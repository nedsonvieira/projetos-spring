package br.com.nedson.api_med_voll.service;

import br.com.nedson.api_med_voll.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//A classe que representa o serviço de autenticação deve implentar a interface
@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repositorio;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return repositorio.findByLogin(login);
    }
}
