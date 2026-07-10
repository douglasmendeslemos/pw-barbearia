// Função para renderizar o menu de acordo com o perfil logado
function renderizarMenuDinamico() {
    const navElement = document.querySelector(".topbar .nav");
    if (!navElement) return;

    const perfil = sessionStorage.getItem("perfil") ? sessionStorage.getItem("perfil").trim().toUpperCase() : null;
    const nomeUsuario = sessionStorage.getItem("nome") ? sessionStorage.getItem("nome").trim() : "";
    const isHomePage = !!document.getElementById("servicos");

    let menuHTML = "";

    if (isHomePage) {
        if (perfil) {
            let portalLink = "";
            if (perfil === "ADMINISTRADOR" || perfil === "BARBEIRO") {
                portalLink = `<a href="/agendamentoBarbeiro">Painel</a>`;
            } else if (perfil === "CLIENTE") {
                portalLink = `<a href="/agendamentoUser">Agendar</a>`;
            }

            menuHTML = `
                <a href="/">Home</a>
                <a href="#servicos">Serviços</a>
                <a href="#barbeiros">Barbeiros</a>
                ${portalLink}
                <span class="user-greeting" style="color: var(--accent); font-weight: bold; margin-left: 10px; margin-right: 10px; align-self: center;">Olá, ${nomeUsuario}</span>
                <a href="#" onclick="logoutUsuario()" style="color: var(--brand); font-weight: bold;">Sair</a>
            `;
        } else {
            // Visitante na Home (não logado)
            menuHTML = `
                <a href="#servicos">Serviços</a>
                <a href="#barbeiros">Barbeiros</a>
                <a href="/auth">Realizar Login</a>
            `;
        }
    } else {
        if (perfil === "ADMINISTRADOR") {
            menuHTML = `
                <a href="/">Home</a>
                <a href="/agendamentoBarbeiro">Agendamentos</a>
                <a href="/agendamentoBarbeiro/servicos">Serviços</a>
                <a href="/perfil">Perfis</a>
                <a href="/barbeiros">Barbeiros</a>
<!--                <a href="/dashboard">Dashboard</a>-->
                <span class="user-greeting" style="color: var(--accent); font-weight: bold; margin-left: 10px; margin-right: 10px; align-self: center;">Olá, ${nomeUsuario}</span>
                <a href="#" onclick="logoutUsuario()" style="color: var(--brand); font-weight: bold;">Sair</a>
            `;
        } else if (perfil === "BARBEIRO") {
            menuHTML = `
                <a href="/">Home</a>
                <a href="/agendamentoBarbeiro">Minha Agenda</a>
                <span class="user-greeting" style="color: var(--accent); font-weight: bold; margin-left: 10px; margin-right: 10px; align-self: center;">Olá, ${nomeUsuario}</span>
                <a href="#" onclick="logoutUsuario()" style="color: var(--brand); font-weight: bold;">Sair</a>
            `;
        } else if (perfil === "CLIENTE") {
            menuHTML = `
                <a href="/">Home</a>
                <a href="/agendamentoUser">Agendar</a>
                <span class="user-greeting" style="color: var(--accent); font-weight: bold; margin-left: 10px; margin-right: 10px; align-self: center;">Olá, ${nomeUsuario}</span>
                <a href="#" onclick="logoutUsuario()" style="color: var(--brand); font-weight: bold;">Sair</a>
            `;
        } else {
            // Usuário não logado (visitante em outra página)
            menuHTML = `
                <a href="/">Início</a>
                <a href="/auth">Entrar</a>
                <a href="/usuarios/cadastro">Cadastrar</a>
            `;
        }
    }

    navElement.innerHTML = menuHTML;
}

// Função global de logout
async function logoutUsuario() {
    try {
        const resposta = await fetch("/auth/logout", {
            method: "POST"
        });

        if (resposta.ok) {
            sessionStorage.clear();
            localStorage.clear();
            window.location.href = "/auth";
        } else {
            alert("Não foi possível realizar logout.");
        }
    } catch (erro) {
        console.error("Erro ao efetuar logout:", erro);
    }
}

// Executa após carregar o DOM
document.addEventListener("DOMContentLoaded", renderizarMenuDinamico);
// Caso o script carregue tarde e o DOM já esteja pronto, executa diretamente
if (document.readyState === "interactive" || document.readyState === "complete") {
    renderizarMenuDinamico();
}
