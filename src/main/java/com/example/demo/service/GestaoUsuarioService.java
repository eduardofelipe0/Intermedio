package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Role;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;

@Service
public class GestaoUsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public Usuario cadastrar(Usuario usuario) {
		String encriptografar = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(encriptografar);
		return usuarioRepository.save(usuario);
	}
	public List<Usuario> buscarNome(String nome) {
		return (List<Usuario>) usuarioRepository.findUsuarioByName(nome);
	}
	public List<Usuario> listar() {
		return (List<Usuario>) usuarioRepository.findAll();
	}
	public List<Usuario> listarPorRole(Role role) {
		return (List<Usuario>) usuarioRepository.findUsuarioByRole(role);
	}
	/*
	public List<Usuario> buscarNomeUsuario(String nomeUsuario) {
		return (List<Usuario>) usuarioRepository.findByUsuarioByNomeUsuario(nomeUsuario).get(0);
	} 
	*/
	public Usuario buscar(Long id) {
		return usuarioRepository.findById(id).get();
	}
	public void deletar(Usuario usuario) {
		usuarioRepository.delete(usuario);
	}
	public Usuario logar(Usuario usuario) {
		return usuario = usuarioRepository.findByNomeUsuarioAndSenha(usuario.getNomeUsuario(), usuario.getSenha());	
	}
}
