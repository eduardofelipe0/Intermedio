package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.exception.NegocioException;
import com.example.demo.model.Entrada;
import com.example.demo.model.StatusEntrada;
import com.example.demo.repository.EntradaRepository;
import com.example.demo.service.GestaoEntradaService;

@Controller
@RequestMapping("/entradas")
public class EntradaController {
	
	@Autowired
	private EntradaRepository entradaRepository;
	@Autowired
	private GestaoEntradaService gestaoEntradaService;
	
	@RequestMapping(value="/new", method=RequestMethod.GET )
	public ModelAndView formulario() {
		ModelAndView modelAndView = new ModelAndView();
		Entrada entrada = new Entrada();
		modelAndView.setViewName("entrada/registro");
		modelAndView.addObject("entradaAtual", entrada);
		return modelAndView;
	}
	
	@RequestMapping(value="/registrar", method=RequestMethod.POST)
	public ModelAndView registrar(Entrada entrada) {
		ModelAndView modelAndView = new ModelAndView();
		
		try {
			gestaoEntradaService.criar(entrada);
			modelAndView.setViewName("entrada/home");
		} catch (Exception e) {
			modelAndView.setViewName("entrada/registro");
		}
		return modelAndView;
	}
	
	@RequestMapping(value="/listar", method=RequestMethod.GET)
	public ModelAndView listarEntradas() {
		ModelAndView modelAndView = new ModelAndView();
		List<Entrada> entradas = gestaoEntradaService.listar();
		modelAndView.setViewName("entrada/listaEntradas");
		modelAndView.addObject("entradas", entradas);
		return modelAndView;
	}
	
	@RequestMapping(value="/editar/{id}", method=RequestMethod.GET )
	public ModelAndView editar(@PathVariable("id") Long id) {
		ModelAndView modelAndView = new ModelAndView();
		Entrada entrada = gestaoEntradaService.buscar(id);
		if(entrada.getStatus() == StatusEntrada.FINALIZADA) {
			throw new NegocioException("Esta entrada já foi finalizada, portanto não é mais possível editá-la.");
		}
		modelAndView.setViewName("entrada/registro");
		modelAndView.addObject("entradaAtual", entrada);
		return modelAndView;
	}
	
	@RequestMapping(value="/excluir/{id}", method=RequestMethod.GET )
	public ModelAndView deletar(@PathVariable Long id) {
		Entrada entrada = entradaRepository.findById(id).get();
		entradaRepository.delete(entrada);
		return listarEntradas();
	}
	
	@RequestMapping(value="/finalizar/{id}", method=RequestMethod.GET)
	public ModelAndView finalizar(@PathVariable Long id) {
		gestaoEntradaService.finalizar(id);
		return listarEntradas();
	}
	
	@RequestMapping(value="/listar/abertas", method=RequestMethod.GET)
	public ModelAndView listarFinalizadas() {
		ModelAndView modelAndView = new ModelAndView();
		List<Entrada> entradas = gestaoEntradaService.listarAbertas();
		modelAndView.setViewName("entrada/listaEntradasAbertas");
		modelAndView.addObject("entradas", entradas);
		return modelAndView;
	}
}
