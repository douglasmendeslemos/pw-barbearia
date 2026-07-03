//const botaoFormulario = document.querySelector("[data-toggle-form]");
//const botoesEtapa = document.querySelectorAll("[data-step-target]");
//function abrirFormulario() {document.getElementById("boxFormulario").classList.toggle("ativo");}
//function mudarTela(idDestino) {document.querySelectorAll(".etapa").forEach((etapa) => {etapa.classList.remove("ativa");});document.getElementById(idDestino).classList.add("ativa");}
//botaoFormulario?.addEventListener("click", abrirFormulario);
//botoesEtapa.forEach((botao) => { botao.addEventListener("click", () => mudarTela(botao.dataset.stepTarget));});
//codigos antigos

async function cadastrarAgendamento() {
    console.log("entrei na função cadastro horario na agenda - agendamento")

    const bookingNotesInput = document.querySelector("#bookingNotes");

    const descricao = bookingNotesInput ? bookingNotesInput.value.trim() : "";
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
    }; //console.log(agendamentoRequestDTO)
    //if (erro) { mostrarMensagem("erro", erro); return;}

    //colocar o fetch aqui


}