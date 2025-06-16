package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import com.google.cloud.vision.v1.EntityAnnotation;

import utils.SummaryUtil;
import utils.VisionUtil;
import utils.VisionUtil2;

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        HttpSession session = request.getSession(false);
//        
//        if (session != null && session.getAttribute("loginedUser") != null) {
//            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/vision/upload.jsp");
//            rd.forward(request, response);
//        } 
//        else {
//            request.setAttribute("error", "ログインをしてください。");
           
//        }
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/vision/upload.jsp");
        rd.forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Part filePart = request.getPart("image");

            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            UUID uuid = UUID.randomUUID();
            String extension = fileName.split("[.]")[1].toLowerCase();
            fileName = uuid + "." + extension;
            
            String uploadPath = getServletContext().getRealPath("") + File.separator + "photos";
            
            Files.createDirectories(Paths.get(uploadPath));
            System.out.println(extension);
            File file = new File(uploadPath, fileName);
            InputStream input = filePart.getInputStream();
            Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            String Path = file.getAbsolutePath();
            if (extension.equals("heic") || extension.equals("heif") || extension.equals("avif")) {
                String jpgFileName = uuid + ".jpg";
                File jpgFile = new File(uploadPath, jpgFileName);

                ProcessBuilder builder = new ProcessBuilder(
                        "ffmpeg",
                        "-i", file.getAbsolutePath(),
                        "-map", "0:60",         
                        "-frames:v", "1",         
                        jpgFile.getAbsolutePath()
                );

                try {
                    Process process = builder.start();

                    try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                        String line;
                        while ((line = errorReader.readLine()) != null) {
                            System.err.println(line);
                        }
                    }

                    int exitCode = process.waitFor();
                    if (exitCode == 0) {
                        fileName = jpgFileName;
                        file.delete();
                        Path = jpgFile.getAbsolutePath();
                    } else {
                        throw new RuntimeException("ffmpeg エラー");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("HEIC エラー");
                }
            }

       
            int score = 0;
            List<EntityAnnotation> result = new ArrayList<>();
            List<String> labels = new ArrayList<>();

            result = VisionUtil.detectLabels(Path);
            //score = VisionUtil.calculateAdvancedArtScore(result);
            score = VisionUtil2.evaluateArtisticScore(Path);
            for (EntityAnnotation ent : result) {

                labels.add(ent.getDescription());
            }
            String summary = SummaryUtil.generateSummary(labels);

            System.out.println(summary);
            
            request.setAttribute("score", score);
            request.setAttribute("labels", result);
            request.setAttribute("filename", fileName);
            request.setAttribute("summary", summary);

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/result.jsp");
            rd.forward(request, response);
            
            } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('エラーが発生しました。'); location.href='index';</script>"); 
            writer.close();
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
       
    }

}
