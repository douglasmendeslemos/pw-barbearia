// Elementos do DOM
const formBarbeiro = document.getElementById("formBarbeiro");
const barbeirosList = document.getElementById("barbeirosList");
const toast = document.getElementById("toast");

// Estado
let barbeiros = [];

// Exibe um toast moderno de sucesso ou erro
function mostrarToast(mensagem, erro = false) {
    toast.textContent = messageText = mensagem;
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

// Carregar lista de barbeiros do banco
async function carregarBarbeiros() {
    try {
        const resposta = await fetch("/barbeiros/api");
        if (resposta.ok) {
            barbeiros = await resposta.json();
            renderizarBarbeiros();
        } else {
            console.error("Erro ao obter barbeiros");
        }
    } catch (error) {
        console.error("Erro de rede ao buscar barbeiros:", error);
    }
}

// Renderiza a lista de barbeiros na tabela
function renderizarBarbeiros() {
    if (barbeiros.length === 0) {
        barbeirosList.innerHTML = `
            <tr>
                <td colspan="5" style="text-align: center; color: var(--muted); padding: 30px;">
                    Nenhum barbeiro cadastrado no sistema.
                </td>
            </tr>
        `;
        return;
    }

    barbeirosList.innerHTML = barbeiros.map(b => `
        <tr>
            <td><strong>${b.id}</strong></td>
            <td>${b.nome}</td>
            <td><span class="avatar-iniciais" style="display: inline-block; background: var(--brand); color: #fff; padding: 4px 8px; border-radius: 4px; font-weight: bold; font-size: 12px;">${b.iniciais || "N/D"}</span></td>
            <td>${b.especialidade || "Geral"}</td>
            <td>${b.usuario ? b.usuario.email : "Sem e-mail"}</td>
        </tr>
    `).join("");
}

// Cadastra um novo barbeiro via API
async function cadastrarBarbeiro(event) {
    event.preventDefault();
    
    const nome = document.getElementById("nome").value.trim();
    const iniciais = document.getElementById("iniciais").value.trim();
    const especialidade = document.getElementById("especialidade").value.trim();
    const email = document.getElementById("email").value.trim();
    const senha = document.getElementById("senha").value.trim();

    if (!nome || !email || !senha) {
        mostrarToast("Nome, E-mail e Senha são obrigatórios.", true);
        return;
    }

    const payload = { nome, iniciais, especialidade, email, senha };

    try {
        const resposta = await fetch("/barbeiros/api", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        });

        const dados = await resposta.json();

        if (resposta.ok) {
            mostrarToast("Barbeiro cadastrado com sucesso!");
            formBarbeiro.reset();
            carregarBarbeiros();
        } else {
            mostrarToast(dados.mensagem || "Erro ao cadastrar barbeiro.", true);
        }
    } catch (error) {
        mostrarToast("Erro de rede com o servidor.", true);
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

// Inicializacao
if (formBarbeiro) {
    formBarbeiro.addEventListener("submit", cadastrarBarbeiro);
}
carregarBarbeiros();
