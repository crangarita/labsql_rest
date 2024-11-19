package link.softbond.service;

import org.springframework.stereotype.Component;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class FileService {

    private static final String FILE_DIRECTORY = "/home/var/labsql";
    private static final String BACKUP_DIRECTORY = "/home/var/labsql/backup";
    private final Path rootLocation = Paths.get(FILE_DIRECTORY);
    private final Path backupLocation = Paths.get(BACKUP_DIRECTORY);

    public FileService() {
        try {
            // Crear directorios si no existen
            Files.createDirectories(rootLocation);
            Files.createDirectories(backupLocation);
        } catch (IOException e) {
            throw new RuntimeException("Error al inicializar directorios", e);
        }
    }

    public void storeFile(MultipartFile file, String name) throws IOException {
        ensureDirectoryExists(rootLocation);

        Path filePath = rootLocation.resolve(name + ".jpg");
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    public void storeBackup(MultipartFile file, String name) throws IOException {
        ensureDirectoryExists(backupLocation);

        Path filePath = backupLocation.resolve(name + ".sql");
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    public Resource loadFile(String filename) {
        try {
            Path file = rootLocation.resolve(filename + ".jpg");
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("El archivo no existe o no es legible: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error al cargar el archivo", e);
        }
    }

    public void deleteFile(String filename) {
        try {
            Path filePath = rootLocation.resolve(filename + ".jpg");
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            } else {
                throw new RuntimeException("El archivo no existe: " + filename);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar el archivo: " + filename, e);
        }
    }

    private void ensureDirectoryExists(Path directory) {
        try {
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al crear el directorio: " + directory, e);
        }
    }
}
