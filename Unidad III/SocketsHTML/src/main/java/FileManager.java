import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileManager {
    // Leer archivo de recursos
    public static String readFile(String filePath) throws IOException {
        var inputStream = FileManager.class.getClassLoader().getResourceAsStream(filePath);

        if (inputStream == null)
            throw new FileNotFoundException("Archivo no encontrado: " + filePath);

        var reader = new BufferedReader(new InputStreamReader(inputStream));
        var content = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }

        return content.toString();
    }

    // Verificar si el archivo existe
    public static boolean fileExists(String filePath) {
        try (var inputStream = FileManager.class.getClassLoader().getResourceAsStream(filePath)) {
            return inputStream != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Leer archivo Binario
    public static byte[] readBinaryFile(String filePath) throws IOException {
        var inputStream = FileManager.class.getClassLoader().getResourceAsStream(filePath);

        if (inputStream == null)
            throw new FileNotFoundException("Archivo no encontrado: " + filePath);

        return inputStream.readAllBytes();
    }
}
