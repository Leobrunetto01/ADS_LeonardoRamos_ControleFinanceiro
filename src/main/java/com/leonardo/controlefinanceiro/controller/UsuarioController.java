package com.leonardo.controlefinanceiro.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leonardo.controlefinanceiro.model.Usuario;
import com.leonardo.controlefinanceiro.repository.UsuarioRepository;
import com.leonardo.controlefinanceiro.repository.ReceitaRepository;
import com.leonardo.controlefinanceiro.repository.DespesaRepository;
import com.leonardo.controlefinanceiro.service.CotacaoService;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private CotacaoService cotacaoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Responsável pela proteção das senhas.
    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    // ABRIR TELA DE CADASTRO
    @GetMapping("/cadastro")
    public String telaCadastro() {
        return "cadastro";
    }

    // CADASTRAR NOVO USUÁRIO
    @PostMapping("/cadastro")
    public String cadastrarUsuario(
            @ModelAttribute Usuario usuario,
            Model model) {

        // Verifica se o e-mail já está cadastrado.
        if (usuarioRepository.findByEmail(
                usuario.getEmail()).isPresent()) {

            model.addAttribute(
                    "erro", "Este e-mail já está cadastrado.");

            return "cadastro";
        }

        // Cria um novo usuário sem aceitar um ID externo.
        Usuario novo = new Usuario();

        novo.setNome(usuario.getNome());
        novo.setEmail(usuario.getEmail());

        // Gera o hash da senha antes de salvar no banco.
        novo.setSenha(
                passwordEncoder.encode(usuario.getSenha()));

        usuarioRepository.save(novo);

        return "redirect:/login";
    }

    // ABRIR TELA DE LOGIN
    @GetMapping("/login")
    public String telaLogin() {
        return "login";
    }

    // VALIDAR LOGIN
    @PostMapping("/login")
    public String fazerLogin(
            @RequestParam String email,
            @RequestParam String senha,
            HttpSession session,
            HttpServletRequest request,
            Model model) {

        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElse(null);

        // Compara a senha informada com o hash armazenado.
        if (usuario != null &&
                passwordEncoder.matches(
                        senha, usuario.getSenha())) {

            // Encerra a sessão anterior e cria uma nova.
            // Isso também descarta o token CSRF anterior.
            session.invalidate();

            HttpSession novaSessao = request.getSession(true);

            // Armazena o identificador do usuário autenticado.
            novaSessao.setAttribute(
                    "usuarioId", usuario.getId());

            return "redirect:/inicio";
        }

        model.addAttribute(
                "erro", "E-mail ou senha inválidos.");

        return "login";
    }

    // PÁGINA INICIAL DO USUÁRIO
    @GetMapping("/inicio")
    public String inicio(HttpSession session, Model model) {

        Object usuarioId = session.getAttribute("usuarioId");

        // Impede o acesso sem autenticação.
        if (!(usuarioId instanceof Long)) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioRepository
                .findById((Long) usuarioId)
                .orElse(null);

        if (usuario == null) {
            session.invalidate();
            return "redirect:/login";
        }

        // Calcula somente as receitas do usuário conectado.
        Double totalReceitas =
                receitaRepository.calcularTotalReceitas(
                        usuario.getId());

        // Calcula somente as despesas do usuário conectado.
        Double totalDespesas =
                despesaRepository.calcularTotalDespesas(
                        usuario.getId());

        // Evita valores nulos nos cálculos financeiros.
        if (totalReceitas == null) {
            totalReceitas = 0.0;
        }

        if (totalDespesas == null) {
            totalDespesas = 0.0;
        }

        // Calcula o saldo individual.
        Double saldo = totalReceitas - totalDespesas;

        // Envia as informações para o dashboard.
        model.addAttribute("usuario", usuario);
        model.addAttribute("totalReceitas", totalReceitas);
        model.addAttribute("totalDespesas", totalDespesas);
        model.addAttribute("saldo", saldo);

        // Integração com a AwesomeAPI.
        try {

            String resposta = cotacaoService.buscarCotacoes();

            JsonNode json = objectMapper.readTree(resposta);

            String dolar = json.path("USDBRL")
                    .path("bid").asText();

            String euro = json.path("EURBRL")
                    .path("bid").asText();

            model.addAttribute("dolar", dolar);
            model.addAttribute("euro", euro);

        } catch (Exception e) {

            // Mantém a tela funcionando se a API falhar.
            model.addAttribute("dolar", "Indisponível");
            model.addAttribute("euro", "Indisponível");
        }

        return "inicio";
    }

    // ENCERRAR SESSÃO UTILIZANDO POST
    @PostMapping("/logout")
    public String logout(HttpSession session) {

        // Encerra a sessão e remove os dados de autenticação.
        session.invalidate();

        return "redirect:/login";
    }
}