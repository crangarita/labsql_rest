package link.softbond.service;

import link.softbond.entities.Consulta;
import link.softbond.entities.Examen;
import link.softbond.entities.Opcion;
import link.softbond.entities.Problema;
import link.softbond.repositorios.ExamenRepository;
import link.softbond.repositorios.OpcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ExamenService {

    @Autowired
    ExamenRepository examenRepository;

    @Autowired
    OpcionRepository opcionRepository;

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public void crearExamen(Examen examen) {
        examen.setToken(generateToken());
        examenRepository.save(examen);

    }

    public List<Opcion> generarExamen(int usuarioId, String examenToken){

        Examen examen = examenRepository.findByToken(examenToken);


        Problema problema = examen.getProblema();
        List<Consulta> consultas = problema.getConsultas();
        List<Opcion> opciones = new ArrayList<>();

        Collections.shuffle(consultas);
        int j = 0;
        for (int i = 0; i < examen.getCantidad(); i++) {
            Consulta consulta = consultas.get(j);
            if(consulta.getEstado()==0){
                Opcion opcion = new Opcion();
                opcion.setConsulta(consulta);
                opcion.setExamen(examen);
                opcion.setUsuario(usuarioId);
                opcionRepository.save(opcion);
            }else{
                i--;
            }
            j++;
        }
        return opciones;
    }

    public static String generateToken() {
        SecureRandom random = new SecureRandom();
        StringBuilder token = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int randomIndex = random.nextInt(ALPHABET.length());
            char randomChar = ALPHABET.charAt(randomIndex);
            token.append(randomChar);
        }

        return token.toString();
    }


}
