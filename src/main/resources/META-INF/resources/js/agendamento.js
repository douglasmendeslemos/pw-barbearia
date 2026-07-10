//const botaoFormulario = document.querySelector("[data-toggle-form]");
//const botoesEtapa = document.querySelectorAll("[data-step-target]");
//function abrirFormulario() {document.getElementById("boxFormulario").classList.toggle("ativo");}
//function mudarTela(idDestino) {document.querySelectorAll(".etapa").forEach((etapa) => {etapa.classList.remove("ativa");});document.getElementById(idDestino).classList.add("ativa");}
//botaoFormulario?.addEventListener("click", abrirFormulario);
//botoesEtapa.forEach((botao) => { botao.addEventListener("click", () => mudarTela(botao.dataset.stepTarget));});
//codigos antigos
let meusAgendamentos = [];
let agendamentoEmEdicaoId = null;
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
    if (!serviceSelect || !serviceSelect.value || !barberSelect || !barberSelect.value || !bookingDate.value || !bookingTime.value) {
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
        const url = agendamentoEmEdicaoId 
            ? `/agendamentoUser/agendar/api/${agendamentoEmEdicaoId}` 
            : "/agendamentoUser/agendar/api";
        const metodo = agendamentoEmEdicaoId ? "PUT" : "POST";

        const resposta = await fetch(url, {
            method: metodo,
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(agendamentoRequestDTO)
        });

        const mensagem = await resposta.text();

        if (!resposta.ok) {
            mostrarMensagem("erro", mensagem || "Nao foi possivel salvar o agendamento.");
            return;
        }

        mostrarMensagem("sucesso", mensagem || "Agendamento salvo com sucesso.");
        
        if (agendamentoEmEdicaoId) {
            cancelarEdicao();
        } else {
            const form = document.getElementById("bookingForm");
            if (form) form.reset();
        }
        
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
    const filtroDataEl = document.getElementById("filtroData");

    if (!listElement) return;

    // Inicializa a data com o dia de hoje caso esteja vazia
    if (filtroDataEl && !filtroDataEl.value) {
        filtroDataEl.value = new Date().toISOString().slice(0, 10);
        // Adiciona o listener uma única vez para recarregar a lista ao mudar a data
        if (!filtroDataEl.dataset.listenerAdded) {
            filtroDataEl.addEventListener("change", carregarMeusAgendamentos);
            filtroDataEl.dataset.listenerAdded = "true";
        }
    }

    try {
        const resposta = await fetch("/agendamentoBarbeiro/meus-agendamentos");
        if (!resposta.ok) {
            listElement.innerHTML = `<div class="empty-state" style="color: var(--brand); padding: 20px; text-align: center;">Não foi possível carregar seus agendamentos.</div>`;
            return;
        }

        let agendamentos = await resposta.json();
        
        // Filtra pela data selecionada se o filtro estiver presente
        if (filtroDataEl && filtroDataEl.value) {
            agendamentos = agendamentos.filter(app => app.dataAgendamento === filtroDataEl.value);
        }
        
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
                    <p style="margin: 3px 0 0; font-size: 13px; color: var(--muted); line-height: 1.4;">
                        Barbeiro: <strong>${app.barbeiroNome}</strong> <br>
                        Cliente: <strong>${app.cliente ? app.cliente.nome : 'N/A'}</strong> <br>
                        Valor: <strong>${(app.valor || 0).toLocaleString("pt-BR", { style: "currency", currency: "BRL" })}</strong>
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

        meusAgendamentos = await resposta.json();
        
        // Ordena por data e hora de forma crescente
        meusAgendamentos.sort((a, b) => {
            const dataComp = a.dataAgendamento.localeCompare(b.dataAgendamento);
            if (dataComp !== 0) return dataComp;
            return a.horaAgendamento.localeCompare(b.horaAgendamento);
        });

        if (meusAgendamentos.length === 0) {
            listElement.innerHTML = `<div class="empty-state" style="color: var(--muted); padding: 20px; text-align: center;">Você não possui agendamentos cadastrados.</div>`;
            return;
        }

        // Renderiza
        listElement.innerHTML = meusAgendamentos.map(app => `
            <article class="appointment-card" style="margin-bottom: 12px;">
                <strong class="appointment-time">${app.horaAgendamento}</strong>
                <div style="display: flex; flex-direction: column; width: 100%;">
                    <span style="font-size: 11px; font-weight: bold; color: var(--accent); text-transform: uppercase; letter-spacing: 0.05em; display: block; margin-bottom: 2px;">
                        Data: ${formatarData(app.dataAgendamento)}
                    </span>
                    <h3 style="margin: 0; font-size: 16px; font-family: Georgia, serif;">${app.servico}</h3>
                    <p style="margin: 3px 0 0; font-size: 13px; color: var(--muted);">
                        Com: <strong>${app.barbeiroNome}</strong> | ${(app.valor || 0).toLocaleString("pt-BR", { style: "currency", currency: "BRL" })}
                    </p>
                    ${app.descricao ? `<p style="margin: 4px 0 0 0; font-size: 12px; font-style: italic; color: var(--muted);">Obs: ${app.descricao}</p>` : ""}
                    <div style="display: flex; gap: 8px; margin-top: 8px;">
                        <button type="button" onclick="iniciarEdicao(${app.id})" style="background: none; border: 1px solid var(--line); border-radius: 6px; padding: 4px 8px; cursor: pointer; color: var(--accent); font-weight: bold; font-size: 11px; transition: 0.2s;">
                            Reagendar / Editar
                        </button>
                        <button type="button" onclick="deletarAgendamentoCliente(${app.id})" style="background: none; border: 1px solid var(--brand); border-radius: 6px; padding: 4px 8px; cursor: pointer; color: var(--brand); font-weight: bold; font-size: 11px; transition: 0.2s;">
                            Desmarcar / Cancelar
                        </button>
                    </div>
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
    
    // Exibe o nome do usuário logado na tela de agendamento
    const usuarioLogadoNomeEl = document.getElementById("usuarioLogadoNome");
    if (usuarioLogadoNomeEl) {
        const nomeUsuario = sessionStorage.getItem("nome");
        if (nomeUsuario) {
            usuarioLogadoNomeEl.textContent = `Cliente: ${nomeUsuario}`;
        }
    }
}

function iniciarEdicao(id) {
    const app = meusAgendamentos.find(a => a.id === id);
    if (!app) return;

    agendamentoEmEdicaoId = id;

    // Seleciona o servico correto
    const servicoEncontrado = services.find(s => s.name === app.servico);
    if (servicoEncontrado) {
        document.getElementById("serviceSelect").value = servicoEncontrado.id;
    }

    // Seleciona o barbeiro correto
    const barbeiroEncontrado = barbers.find(b => b.name === app.barbeiroNome);
    if (barbeiroEncontrado) {
        document.getElementById("barberSelect").value = barbeiroEncontrado.id;
    }

    document.getElementById("bookingDate").value = app.dataAgendamento;
    document.getElementById("bookingTime").value = app.horaAgendamento;
    document.getElementById("bookingNotes").value = app.descricao || "";

    // Feedback visual de edicao
    document.getElementById("booking-title").textContent = "Reagendar Atendimento";
    
    const submitBtn = document.querySelector("#bookingForm button.submit");
    if (submitBtn) {
        submitBtn.textContent = "Salvar Alterações";
    }

    if (!document.getElementById("btnCancelarEdicao")) {
        const btnCancel = document.createElement("button");
        btnCancel.id = "btnCancelarEdicao";
        btnCancel.type = "button";
        btnCancel.className = "button secondary";
        btnCancel.style.marginLeft = "10px";
        btnCancel.style.padding = "10px 14px";
        btnCancel.style.borderRadius = "8px";
        btnCancel.style.fontWeight = "bold";
        btnCancel.style.border = "1px solid var(--line)";
        btnCancel.style.cursor = "pointer";
        btnCancel.textContent = "Cancelar Edição";
        btnCancel.onclick = cancelarEdicao;
        
        const container = submitBtn.parentNode;
        container.appendChild(btnCancel);
    }

    document.getElementById("bookingForm").scrollIntoView({ behavior: 'smooth' });
}

function cancelarEdicao() {
    agendamentoEmEdicaoId = null;
    const form = document.getElementById("bookingForm");
    if (form) form.reset();
    
    document.getElementById("booking-title").textContent = "Agendar atendimento";
    
    const submitBtn = document.querySelector("#bookingForm button.submit");
    if (submitBtn) {
        submitBtn.textContent = "Confirmar agendamento";
    }
    
    const btnCancel = document.getElementById("btnCancelarEdicao");
    if (btnCancel) {
        btnCancel.remove();
    }
}

async function deletarAgendamentoCliente(id) {
    if (!confirm("Tem certeza que deseja desmarcar este atendimento?")) {
        return;
    }

    try {
        const resposta = await fetch(`/agendamentoUser/agendar/api/${id}`, {
            method: "DELETE"
        });

        if (resposta.ok) {
            mostrarMensagem("sucesso", "Agendamento desmarcado com sucesso.");
            if (agendamentoEmEdicaoId === id) {
                cancelarEdicao();
            }
            carregarAgendamentosDoCliente();
        } else {
            const erroMsg = await resposta.text();
            mostrarMensagem("erro", erroMsg || "Não foi possível desmarcar o agendamento.");
        }
    } catch (error) {
        mostrarMensagem("erro", "Erro de conexão com o servidor.");
    }
}