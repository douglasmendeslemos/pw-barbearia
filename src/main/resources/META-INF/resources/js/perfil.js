// Estado da aplicação
let perfis = [];
let usuarios = [];

// Elementos da tela
const usuariosList = document.getElementById("usuariosList");
const toast = document.getElementById("toast");

// Exibe um toast moderno de sucesso ou erro
function mostrarToast(mensagem, erro = false) {
    toast.textContent = mensagem;
    if (erro) {
        toast.classList.add("error");
    } else {
        toast.classList.remove("error");
    }
    toast.classList.add("show");
    
    setTimeout(() => {
        toast.classList.remove("show");
    }, 3000);
}

// Busca a lista de perfis cadastrados no banco
async function carregarPerfis() {
    try {
        const resposta = await fetch("/perfil/list-perfis");
        if (resposta.ok) {
            perfis = await resposta.json();
        } else {
            console.error("Erro ao carregar perfis");
        }
    } catch (error) {
        console.error("Erro de rede ao buscar perfis:", error);
    }
}

// Busca a lista de usuários cadastrados no banco
async function carregarUsuarios() {
    try {
        const resposta = await fetch("/perfil/usuarios");
        if (resposta.ok) {
            usuarios = await resposta.json();
        } else {
            usuariosList.innerHTML = `
                <tr>
                    <td colspan="4" style="text-align: center; color: var(--brand); padding: 30px;">
                        Erro ao carregar os usuários do sistema.
                    </td>
                </tr>
            `;
            console.error("Erro ao carregar usuarios");
        }
    } catch (error) {
        usuariosList.innerHTML = `
            <tr>
                <td colspan="4" style="text-align: center; color: var(--brand); padding: 30px;">
                    Erro de conexão com o servidor.
                </td>
            </tr>
        `;
        console.error("Erro de rede ao buscar usuarios:", error);
    }
}

// Renderiza a tabela de usuários com os dropdowns de perfil
function renderizarUsuarios() {
    if (usuarios.length === 0) {
        usuariosList.innerHTML = `
            <tr>
                <td colspan="4" style="text-align: center; color: var(--muted); padding: 30px;">
                    Nenhum usuário cadastrado.
                </td>
            </tr>
        `;
        return;
    }

    usuariosList.innerHTML = usuarios.map(usuario => {
        // Cria as opções do dropdown de perfis dinamicamente
        const optionsHTML = perfis.map(perfil => {
            const isSelected = perfil.id === usuario.perfilId ? "selected" : "";
            return `<option value="${perfil.id}" ${isSelected}>${perfil.nomePerfil}</option>`;
        }).join("");

        return `
            <tr>
                <td><strong>${usuario.id}</strong></td>
                <td>${usuario.nome}</td>
                <td>${usuario.email}</td>
                <td>
                    <select onchange="atualizarPerfilUsuario(${usuario.id}, this.value)" aria-label="Selecionar privilégio do usuário">
                        ${optionsHTML}
                    </select>
                </td>
            </tr>
        `;
    }).join("");
}

// Atualiza o privilégio do usuário no banco via fetch API
async function atualizarPerfilUsuario(usuarioId, perfilId) {
    try {
        const resposta = await fetch(`/perfil/usuarios/alterar-perfil?usuarioId=${usuarioId}&perfilId=${perfilId}`, {
            method: "POST"
        });

        if (resposta.ok) {
            mostrarToast("Privilégio do usuário atualizado com sucesso!");
        } else {
            const erroMsg = await resposta.text();
            mostrarToast(erroMsg || "Não foi possível atualizar o privilégio.", true);
            // Recarrega para voltar o select ao valor correto anterior
            inicializar();
        }
    } catch (error) {
        mostrarToast("Erro de comunicação com o servidor.", true);
        inicializar();
    }
}

// Efetua logout
async function logout() {
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
        console.error(erro);
    }
}

// Inicialização da tela
async function inicializar() {
    await carregarPerfis();
    await carregarUsuarios();
    renderizarUsuarios();
}

// Disparar carga de dados assim que o script terminar de ler
inicializar();
