package link.softbond.service;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
@Component
public class FileService {
	private static final String FILE_DIRECTORY = "/home/var/labsql";
	private final Path rootLocation = Paths.get(FILE_DIRECTORY);

	public void storeFile(MultipartFile file,String name) throws IOException {
		
		Path filePath = Paths.get(FILE_DIRECTORY +"/"+ name+".jpg");
		
		Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
	}

	public Resource loadFile(String filename) {
		try {
			Path file = rootLocation.resolve(filename+".jpg");
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				
				throw new RuntimeException("FAIL NO EXISTE!"+filename);
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("FAIL!");
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

}
