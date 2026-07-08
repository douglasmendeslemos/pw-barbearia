let services = [];

const barbers = [
    {
        id: "marcos",
        name: "Marcos",
        initials: "MA",
        specialty: "Especialista em degradê e cortes clássicos."
    },
    {
        id: "renan",
        name: "Renan",
        initials: "RE",
        specialty: "Barba, navalha e atendimento premium."
    },
    {
        id: "lucas",
        name: "Lucas",
        initials: "LU",
        specialty: "Cortes modernos, freestyle e finalização."
    }
];

const timeSlots = [
    "09:00",
    "09:40",
    "10:20",
    "11:00",
    "13:00",
    "13:40",
    "14:20",
    "15:00",
    "15:40",
    "16:20",
    "17:00",
    "17:40",
    "18:20"
];

const storageKey = "barbearia-prime-agendamentos";

const serviceGrid = document.querySelector("#serviceGrid");
const barberGrid = document.querySelector("#barberGrid");
const serviceSelect = document.querySelector("#serviceSelect");
const barberSelect = document.querySelector("#barberSelect");
const bookingTime = document.querySelector("#bookingTime");
const bookingDate = document.querySelector("#bookingDate");
const bookingForm = document.querySelector("#bookingForm");
const appointmentList = document.querySelector("#appointmentList");
const totalAppointments = document.querySelector("#totalAppointments");
const dayRevenue = document.querySelector("#dayRevenue");
const clearSchedule = document.querySelector("#clearSchedule");
const toast = document.querySelector("#toast");

let appointments = loadAppointments();

function money(value) {
    return value.toLocaleString("pt-BR", {
        style: "currency",
        currency: "BRL",
        maximumFractionDigits: 0
    });
}

function todayISO() {
    return new Date().toISOString().slice(0, 10);
}

function loadAppointments() {
    const saved = localStorage.getItem(storageKey);
    return saved ? JSON.parse(saved) : [];
}
//save temporario
function saveAppointments() {localStorage.setItem(storageKey, JSON.stringify(appointments));}

function showToast(message) {
    toast.textContent = message;
    toast.classList.add("is-visible");
    window.setTimeout(() => toast.classList.remove("is-visible"), 2600);
}
function showToast(message) {
    // 1. Verifica se a variável toast encontrou o elemento no HTML
    if (toast) {
        // Se encontrou, mostra a notificação bonita do CSS
        toast.textContent = message;
        toast.classList.add("is-visible");
        window.setTimeout(() => toast.classList.remove("is-visible"), 2600);
    } else {
        // 2. Se o elemento não existir no HTML, aciona o alert padrão do navegador
        alert(message);
    }
}

function renderServices() {
    // 1. Tenta renderizar os cartões (se a div existir na tela)
    if (serviceGrid) {
        serviceGrid.innerHTML = services.map(service => `
      <article class="service-card">
        <h3>${service.name}</h3>
        <p>${service.description}</p>
        <div class="meta">
          <span>${service.duration} min</span>
          <span>${money(service.price)}</span>
        </div>
      </article>
    `).join("");
    }

    // 2. Tenta preencher as opções do formulário (se o select existir na tela)
    if (serviceSelect) {
        serviceSelect.innerHTML = services.map(service => `
      <option value="${service.id}">${service.name} - ${money(service.price)}</option>
    `).join("");
    }
}

function renderBarbers() {
    // 1. Tenta renderizar os cartões de barbeiros
    if (barberGrid) {
        barberGrid.innerHTML = barbers.map(barber => `
      <article class="barber-card">
        <span class="avatar">${barber.initials}</span>
        <h3>${barber.name}</h3>
        <p>${barber.specialty}</p>
        <div class="meta">
          <span>Disponível hoje</span>
        </div>
      </article>
    `).join("");
    }

    // 2. Tenta preencher as opções do formulário
    if (barberSelect) {
        barberSelect.innerHTML = barbers.map(barber => `
      <option value="${barber.id}">${barber.name}</option>
    `).join("");
    }
}

function renderTimes() {
    if (!bookingTime) return; // Only render if the booking time select exists
    bookingTime.innerHTML = timeSlots.map(time => `<option value="${time}">${time}</option>`).join("");
}

function getService(id) {
    return services.find(service => service.id == id);
}

function getBarber(id) {
    return barbers.find(barber => barber.id === id);
}

function filteredAppointments() {
    if (!bookingDate) return []; // Return empty array if booking date input is missing
    return appointments
        .filter(appointment => appointment.date === bookingDate.value)
        .sort((a, b) => a.time.localeCompare(b.time));
}

function renderAppointments() {
    if (!appointmentList || !totalAppointments || !dayRevenue) return;

    const dayAppointments = filteredAppointments();
    const revenue = dayAppointments.reduce((sum, appointment) => sum + appointment.price, 0);

    totalAppointments.textContent = dayAppointments.length;
    dayRevenue.textContent = money(revenue);

    if (!dayAppointments.length) {
        appointmentList.innerHTML = `<div class="empty-state">Nenhum horário agendado para esta data.</div>`;
        return;
    }

    // Atualizado para não mostrar as variáveis client e phone
    appointmentList.innerHTML = dayAppointments.map(appointment => `
    <article class="appointment-card">
      <strong class="appointment-time">${appointment.time}</strong>
      <div>
        <h3>${appointment.serviceName}</h3>
        <p>Com: ${appointment.barberName}</p>
        ${appointment.notes ? `<p>Obs: ${appointment.notes}</p>` : ""}
      </div>
      <button class="remove-button" type="button" data-id="${appointment.id}" aria-label="Remover agendamento">×</button>
    </article>
  `).join("");
}

function resetForm() {
    if (bookingForm && bookingDate) {
        bookingForm.reset();
        bookingDate.value = todayISO();
        renderAppointments();
    }
}

function handleSubmit(event) {
    event.preventDefault();

    const bookingNotesInput = document.querySelector("#bookingNotes");

    // Verifica se os campos vitais para o sistema existem
    if (!serviceSelect || !barberSelect || !bookingDate || !bookingTime) {
        console.error("Faltam campos obrigatórios (Serviço, Barbeiro, Data ou Hora).");
        return;
    }

    const notes = bookingNotesInput ? bookingNotesInput.value.trim() : "";
    const service = getService(serviceSelect.value);
    const barber = getBarber(barberSelect.value);
    const date = bookingDate.value;
    const time = bookingTime.value;

    // Verifica conflito de horário
    const conflict = appointments.some(appointment =>
        appointment.date === date &&
        appointment.time === time &&
        appointment.barberId === barber.id
    );

    if (conflict) {
        showToast("Esse barbeiro já tem atendimento neste horário.");
        return;
    }

    // Salva o agendamento SEM o client e phone
    appointments.push({
        id: crypto.randomUUID(),
        serviceId: service.id,
        serviceName: service.name,
        price: service.price,
        barberId: barber.id,
        barberName: barber.name,
        date,
        time,
        notes
    });

    saveAppointments();
    resetForm();
    showToast("Agendamento confirmado com sucesso!");
}

function handleListClick(event) {
    const button = event.target.closest("[data-id]");
    if (!button) return;

    appointments = appointments.filter(appointment => appointment.id !== button.dataset.id);
    saveAppointments();
    renderAppointments();
    showToast("Agendamento removido.");
}

function handleClear() {
    if(!bookingDate) return;

    const date = bookingDate.value;
    const hasAppointments = appointments.some(appointment => appointment.date === date);

    if (!hasAppointments) {
        showToast("Não há horários para limpar nesta data.");
        return;
    }

    appointments = appointments.filter(appointment => appointment.date !== date);
    saveAppointments();
    renderAppointments();
    showToast("Agenda da data limpa.");
}

async function carregarServicosDoBanco() {
    try {
        const resposta = await fetch("/api/servicos");
        if (resposta.ok) {
            const dadosDoBanco = await resposta.json();
            services = dadosDoBanco.map(servico => ({
                id: servico.id,
                name: servico.nome,
                description: servico.descricao,
                duration: servico.duracaoMinutos,
                price: servico.valor
            }));
            renderServices();
        } else {
            console.error("Falha ao buscar os servicos do servidor.");
        }
    } catch (error) {
        console.error("Erro de rede ao buscar os servicos:", error);
    }
}

// Initial render calls
carregarServicosDoBanco();
renderBarbers();
renderTimes();

// Initialize date input if it exists
if (bookingDate) {
    bookingDate.min = todayISO();
    bookingDate.value = todayISO();
}

async function logout() {
    console.log("Cliquei em sair");

    try {
        const resposta = await fetch("/auth/logout", {
            method: "POST"
        });

        if (resposta.ok) {
            // Limpa dados armazenados no navegador
            sessionStorage.clear();
            localStorage.clear();

            // Redireciona para a tela de login
            window.location.href = "/auth";
        } else {
            alert("Não foi possível realizar logout.");
        }
    } catch (erro) {
        console.error(erro);
    }
}
renderAppointments();


// Event Listeners - Wrap in if statements to prevent errors if elements are not on the page
if (bookingForm) {
    bookingForm.addEventListener("submit", handleSubmit);
}

if (bookingDate) {
    bookingDate.addEventListener("change", renderAppointments);
}

if (appointmentList) {
    appointmentList.addEventListener("click", handleListClick);
}

if (clearSchedule) {
    clearSchedule.addEventListener("click", handleClear);
}