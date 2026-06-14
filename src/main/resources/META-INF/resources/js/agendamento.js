const botaoFormulario = document.querySelector("[data-toggle-form]");
const botoesEtapa = document.querySelectorAll("[data-step-target]");

function abrirFormulario() {
    document.getElementById("boxFormulario").classList.toggle("ativo");
}

function mudarTela(idDestino) {
    document.querySelectorAll(".etapa").forEach((etapa) => {
        etapa.classList.remove("ativa");
    });

    document.getElementById(idDestino).classList.add("ativa");
}

botaoFormulario?.addEventListener("click", abrirFormulario);

botoesEtapa.forEach((botao) => {
    botao.addEventListener("click", () => mudarTela(botao.dataset.stepTarget));
});
