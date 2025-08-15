package com.webserver;

public class MimeTypesDetector {

    /**
     * Obtiene el tipo MIME basado en la extensión del archivo
     */
    public static String getMimeType(String filename) {
        String extension = RequestHandler.getFileExtension(filename);
        switch (extension) {
            case "html":
                return "text/html; charset=UTF-8";
            case "css":
                return "text/css; charset=UTF-8";
            case "js":
                return "application/javascript; charset=UTF-8";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "webp":
                return "image/webp";
            case "avif":
                return "image/avif";
            case "txt":
                return "text/plain; charset=UTF-8";
            default:
                return "application/octet-stream";
        }
    }

    /**
     * Obtiene el tipo MIME específico para imágenes
     */
    public static String getMimeTypeForImage(String extension) {
        switch (extension.toLowerCase()) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "webp":
                return "image/webp";
            case "avif":
                return "image/avif";
            case "bmp":
                return "image/bmp";
            case "svg":
                return "image/svg+xml";
            default:
                return "image/jpeg";
        }
    }

    /**
     * Verifica si un archivo es una imagen
     */
    public static boolean isImage(String filename) {
        String extension = RequestHandler.getFileExtension(filename);
        return extension.matches("jpg|jpeg|png|gif|webp|avif|bmp|svg");
    }

    /**
     * Verifica si un archivo es texto
     */
    public static boolean isText(String filename) {
        String extension = RequestHandler.getFileExtension(filename);
        return extension.matches("html|css|js|txt|json|xml");
    }
}
