const formCadastroUsuario = document.getElementById("formCadastroUsuario");
const mensagemCadastro = document.getElementById("mensagemCadastro");
const formCadastroServico = document.getElementById("formCadastroServico");

function mostrarMensagem(tipo, texto) {
    mensagemCadastro.textContent = texto;
    mensagemCadastro.classList.remove("hidden", "alert-error", "alert-success");
    mensagemCadastro.classList.add(tipo === "sucesso" ? "alert-success" : "alert-error");
}

function limparMensagem() {
    mensagemCadastro.textContent = "";
    mensagemCadastro.classList.add("hidden");
    mensagemCadastro.classList.remove("alert-error", "alert-success");
}

function validarFormulario(dados) {
    const nome = dados.get("nome")?.trim();
    const email = dados.get("email")?.trim();
    const senha = dados.get("senha");
    const confirmacaoSenha = dados.get("confirmacaoSenha");

    if (!nome || !email || !senha || !confirmacaoSenha) {
        return "Preencha todos os campos.";
    }

    if (senha !== confirmacaoSenha) {
        return "A senha e a confirmacao devem ser iguais.";
    }

    return null;
}

async function cadastrarUsuario(event) {
    event.preventDefault();
    limparMensagem();

    const botaoSubmit = formCadastroUsuario.querySelector("button[type='submit']");
    const dados = new FormData(formCadastroUsuario);
    const erro = validarFormulario(dados);

    if (erro) {
        mostrarMensagem("erro", erro);
        return;
    }

    botaoSubmit.disabled = true;
    botaoSubmit.textContent = "Cadastrando...";

    try {
        const resposta = await fetch("/usuarios/cadastro/api", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: new URLSearchParams(dados)
        });

        const mensagem = await resposta.text();

        if (!resposta.ok) {
            mostrarMensagem("erro", mensagem || "Nao foi possivel cadastrar o usuario.");
            return;
        }

        formCadastroUsuario.reset();
        mostrarMensagem("sucesso", mensagem || "Usuario cadastrado com sucesso.");
    } catch (error) {
        mostrarMensagem("erro", "Erro de comunicacao com o servidor.");
    } finally {
        botaoSubmit.disabled = false;
        botaoSubmit.textContent = "Cadastrar";
    }
}

formCadastroUsuario?.addEventListener("submit", cadastrarUsuario);
formCadastroServico?.addEventListener("submit", cadastrarServico);

async function cadastrarServico() {
    limparMensagem();

    const botaoSubmit = formCadastroServico.querySelector("button[type='submit']");
    const dados = new FormData(formCadastroServico);
    console.log("entrei na função cadastro de Serviço")
    const nomeDigitado = document.getElementById('nome').value
    const descricaoDigitado = document.getElementById('descricao').value
    const tempoDigitado = parseInt(document.getElementById('tempo').value)
    const valorDigitado = parseFloat(document.getElementById('valor').value)


    const ServicoRequestDTO = {
        nome: nomeDigitado,
        descricao: descricaoDigitado,
        valor: valorDigitado,
        duracaoMinutos: tempoDigitado
    };

    //if (erro) { mostrarMensagem("erro", erro); return;}

    botaoSubmit.disabled = true;
    botaoSubmit.textContent = "Cadastrando...";

    try {
        const resposta = await fetch("/agendamentoBarbeiro/servicos/api", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(ServicoRequestDTO)
        });

        const mensagem = await resposta.text();

        if (!resposta.ok) {
            mostrarMensagem("erro", mensagem || "Nao foi possivel cadastrar o servico.");
            return;
        }

        formCadastroServico.reset();
        mostrarMensagem("sucesso", mensagem || "servico cadastrado com sucesso.");
    } catch (error) {
        mostrarMensagem("erro", "Erro de comunicacao com o servidor.");
    } finally {
        botaoSubmit.disabled = false;
        botaoSubmit.textContent = "Cadastrar";
    }
}
