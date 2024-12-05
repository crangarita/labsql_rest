package link.softbond.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import link.softbond.service.DBService;
import link.softbond.util.Response;

@RestController
@RequestMapping("/database")
public class DatabaseController {

	@Autowired
    private  DBService dbService;

	@PostMapping("/restore/{nombrebase}")
    public Response restoreDatabase(@PathVariable String nombrebase, @RequestPart("file") MultipartFile file) {
        try {
            dbService.restoreDatabaseFromBackup(file, nombrebase);
            return Response.crear(true, "Base de datos restaurada exitosamente.", "Base: " + nombrebase);
        } catch (Exception e) {
            return Response.crear(false, "Error al restaurar la base de datos", e.getMessage());
        }
    }
}

