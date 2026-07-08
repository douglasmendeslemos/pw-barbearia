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
        valor: servico.price,
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
        
        // Se estiver no painel do barbeiro, recarrega a lista de agendamentos após cadastrar
        if (document.getElementById("appointmentList")) {
            carregarMeusAgendamentos();
        }
        
        // Se estiver na tela do cliente, recarrega a lista de agendamentos do cliente
        if (document.getElementById("meusAgendamentosList")) {
            carregarAgendamentosDoCliente();
        }
    } catch (error) {
        mostrarMensagem("erro", "Erro de comunicacao com o servidor.");
    }
}

// Funções para o painel de agendamentos do barbeiro
async function carregarMeusAgendamentos() {
    const listElement = document.getElementById("appointmentList");
    const totalElement = document.getElementById("totalAppointments");
    const revenueElement = document.getElementById("dayRevenue");

    if (!listElement) return;

    try {
        const resposta = await fetch("/agendamentoBarbeiro/meus-agendamentos");
        if (!resposta.ok) {
            listElement.innerHTML = `<div class="empty-state" style="color: var(--brand); padding: 20px; text-align: center;">Não foi possível carregar seus agendamentos.</div>`;
            return;
        }

        const agendamentos = await resposta.json();
        
        // Ordena por data e hora de forma crescente
        agendamentos.sort((a, b) => {
            const dataComp = a.dataAgendamento.localeCompare(b.dataAgendamento);
            if (dataComp !== 0) return dataComp;
            return a.horaAgendamento.localeCompare(b.horaAgendamento);
        });

        // Atualiza estatísticas
        if (totalElement) {
            totalElement.textContent = agendamentos.length;
        }
        if (revenueElement) {
            const totalFaturamento = agendamentos.reduce((sum, item) => sum + (item.valor || 0), 0);
            revenueElement.textContent = totalFaturamento.toLocaleString("pt-BR", {
                style: "currency",
                currency: "BRL"
            });
        }

        if (agendamentos.length === 0) {
            listElement.innerHTML = `<div class="empty-state" style="color: var(--muted); padding: 20px; text-align: center;">Nenhum agendamento localizado para você.</div>`;
            return;
        }

        // Renderiza
        listElement.innerHTML = agendamentos.map(app => `
            <article class="appointment-card" style="margin-bottom: 12px;">
                <strong class="appointment-time">${app.horaAgendamento}</strong>
                <div>
                    <span style="font-size: 12px; font-weight: bold; color: var(--accent); text-transform: uppercase; letter-spacing: 0.05em; display: block; margin-bottom: 2px;">
                        Data: ${formatarData(app.dataAgendamento)}
                    </span>
                    <h3 style="margin: 0; font-size: 16px; font-family: Georgia, serif;">${app.servico}</h3>
                    <p style="margin: 3px 0 0; font-size: 13px; color: var(--muted);">
                        Valor: ${(app.valor || 0).toLocaleString("pt-BR", { style: "currency", currency: "BRL" })}
                    </p>
                    ${app.descricao ? `<p style="margin: 4px 0 0; font-size: 12px; font-style: italic; color: var(--muted);">Obs: ${app.descricao}</p>` : ""}
                </div>
            </article>
        `).join("");

    } catch (error) {
        console.error("Erro ao carregar agendamentos:", error);
        listElement.innerHTML = `<div class="empty-state" style="color: var(--brand); padding: 20px; text-align: center;">Erro de conexão com o servidor.</div>`;
    }
}

// Auxiliar para deixar a data mais legível (AAAA-MM-DD -> DD/MM/AAAA)
function formatarData(dataString) {
    if (!dataString) return "";
    const partes = dataString.split("-");
    if (partes.length === 3) {
        return `${partes[2]}/${partes[1]}/${partes[0]}`;
    }
    return dataString;
}

// Inicializa a carga condicionalmente ao carregar o script
if (document.getElementById("appointmentList")) {
    carregarMeusAgendamentos();
}

async function carregarAgendamentosDoCliente() {
    const listElement = document.getElementById("meusAgendamentosList");
    if (!listElement) return;

    try {
        const resposta = await fetch("/agendamentoUser/meus-agendamentos");
        if (!resposta.ok) {
            listElement.innerHTML = `<div class="empty-state" style="color: var(--brand); padding: 20px; text-align: center;">Não foi possível carregar seus agendamentos.</div>`;
            return;
        }

        const agendamentos = await resposta.json();
        
        // Ordena por data e hora de forma crescente
        agendamentos.sort((a, b) => {
            const dataComp = a.dataAgendamento.localeCompare(b.dataAgendamento);
            if (dataComp !== 0) return dataComp;
            return a.horaAgendamento.localeCompare(b.horaAgendamento);
        });

        if (agendamentos.length === 0) {
            listElement.innerHTML = `<div class="empty-state" style="color: var(--muted); padding: 20px; text-align: center;">Você não possui agendamentos cadastrados.</div>`;
            return;
        }

        // Renderiza
        listElement.innerHTML = agendamentos.map(app => `
            <article class="appointment-card" style="margin-bottom: 12px;">
                <strong class="appointment-time">${app.horaAgendamento}</strong>
                <div>
                    <span style="font-size: 11px; font-weight: bold; color: var(--accent); text-transform: uppercase; letter-spacing: 0.05em; display: block; margin-bottom: 2px;">
                        Data: ${formatarData(app.dataAgendamento)}
                    </span>
                    <h3 style="margin: 0; font-size: 16px; font-family: Georgia, serif;">${app.servico}</h3>
                    <p style="margin: 3px 0 0; font-size: 13px; color: var(--muted);">
                        Com: <strong>${app.barbeiroNome}</strong> | ${(app.valor || 0).toLocaleString("pt-BR", { style: "currency", currency: "BRL" })}
                    </p>
                    ${app.descricao ? `<p style="margin: 4px 0 0 0; font-size: 12px; font-style: italic; color: var(--muted);">Obs: ${app.descricao}</p>` : ""}
                </div>
            </article>
        `).join("");

    } catch (error) {
        console.error("Erro ao carregar agendamentos do cliente:", error);
        listElement.innerHTML = `<div class="empty-state" style="color: var(--brand); padding: 20px; text-align: center;">Erro de conexão com o servidor.</div>`;
    }
}

// Inicializa a carga condicionalmente ao carregar o script para o cliente
if (document.getElementById("meusAgendamentosList")) {
    carregarAgendamentosDoCliente();
}