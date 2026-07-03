//const botaoFormulario = document.querySelector("[data-toggle-form]");
//const botoesEtapa = document.querySelectorAll("[data-step-target]");
//function abrirFormulario() {document.getElementById("boxFormulario").classList.toggle("ativo");}
//function mudarTela(idDestino) {document.querySelectorAll(".etapa").forEach((etapa) => {etapa.classList.remove("ativa");});document.getElementById(idDestino).classList.add("ativa");}
//botaoFormulario?.addEventListener("click", abrirFormulario);
//botoesEtapa.forEach((botao) => { botao.addEventListener("click", () => mudarTela(botao.dataset.stepTarget));});
//codigos antigos
const mensagemCadastro = document.getElementById("mensagemCadastro");
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

async function cadastrarAgendamento(event) {
    limparMensagem()
    console.log("entrei na função cadastro horario na agenda - agendamento")


    // 1. Evita que a página recarregue ao submeter o form
    if (event) {
        event.preventDefault();
    }

    limparMensagem();
    console.log("Iniciando o cadastro do agendamento...");

    // Verifica se os campos vitais existem e estão preenchidos
    if (!serviceSelect || !barberSelect || !bookingDate.value || !bookingTime.value) {
        mostrarMensagem("erro", "Por favor, preencha todos os campos obrigatórios.");
        return;
    }

    const bookingNotesInput = document.querySelector("#bookingNotes");

    const descricao = bookingNotesInput ? bookingNotesInput.value.trim() : "sem descricao";
    const servico = getService(serviceSelect.value);
    const barbeiro = getBarber(barberSelect.value);
    const date = bookingDate.value;
    const time = bookingTime.value;



    const agendamentoRequestDTO = {
        servico: servico.name,
        preco: servico.price,
        descricao: descricao,
        barbeiro: barbeiro.name,
        data: date,
        hora: time,
    }; console.log(agendamentoRequestDTO)
    //if (erro) { mostrarMensagem("erro", erro); return;}

    //colocar o fetch aqui
    try {
        const resposta = await fetch("/agendamentoUser/agendar/api", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(agendamentoRequestDTO)
        });

        const mensagem = await resposta.text();

        if (!resposta.ok) {
            mostrarMensagem("erro", mensagem || "Nao foi possivel AGENDAR o servico.");
            return;
        }

        mostrarMensagem("sucesso", mensagem || "servico AGENDADO com sucesso.");
    } catch (error) {
        mostrarMensagem("erro", "Erro de comunicacao com o servidor.");
    }

}