package pe.com.market.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pe.com.market.model.Usuario;
import pe.com.market.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public Usuario login(String username, String password) {

        Usuario user = repo.findByUsernameAndEstado(username, true);

        if (user != null && encoder.matches(password, user.getPassword())) {
            return user;
        }

        return null;
    }
}
