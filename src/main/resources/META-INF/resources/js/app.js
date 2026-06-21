const services = [
  {
    id: "corte",
    name: "Corte masculino",
    description: "Tesoura, máquina, acabamento e finalização.",
    duration: 40,
    price: 45
  },
  {
    id: "barba",
    name: "Barba completa",
    description: "Toalha quente, navalha, hidratação e balm.",
    duration: 30,
    price: 35
  },
  {
    id: "combo",
    name: "Corte + barba",
    description: "Atendimento completo para renovar o visual.",
    duration: 70,
    price: 75
  },
  {
    id: "sobrancelha",
    name: "Sobrancelha",
    description: "Limpeza e alinhamento com acabamento natural.",
    duration: 15,
    price: 20
  }
];

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

function saveAppointments() {
  localStorage.setItem(storageKey, JSON.stringify(appointments));
}

function showToast(message) {
  toast.textContent = message;
  toast.classList.add("is-visible");
  window.setTimeout(() => toast.classList.remove("is-visible"), 2600);
}

function renderServices() {
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

  serviceSelect.innerHTML = services.map(service => `
    <option value="${service.id}">${service.name} - ${money(service.price)}</option>
  `).join("");
}

function renderBarbers() {
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

  barberSelect.innerHTML = barbers.map(barber => `
    <option value="${barber.id}">${barber.name}</option>
  `).join("");
}

function renderTimes() {
  bookingTime.innerHTML = timeSlots.map(time => `<option value="${time}">${time}</option>`).join("");
}

function getService(id) {
  return services.find(service => service.id === id);
}

function getBarber(id) {
  return barbers.find(barber => barber.id === id);
}

function filteredAppointments() {
  return appointments
    .filter(appointment => appointment.date === bookingDate.value)
    .sort((a, b) => a.time.localeCompare(b.time));
}

function renderAppointments() {
  const dayAppointments = filteredAppointments();
  const revenue = dayAppointments.reduce((sum, appointment) => sum + appointment.price, 0);

  totalAppointments.textContent = dayAppointments.length;
  dayRevenue.textContent = money(revenue);

  if (!dayAppointments.length) {
    appointmentList.innerHTML = `<div class="empty-state">Nenhum horário agendado para esta data.</div>`;
    return;
  }

  appointmentList.innerHTML = dayAppointments.map(appointment => `
    <article class="appointment-card">
      <strong class="appointment-time">${appointment.time}</strong>
      <div>
        <h3>${appointment.client}</h3>
        <p>${appointment.serviceName} com ${appointment.barberName}</p>
        <p>${appointment.phone}${appointment.notes ? ` • ${appointment.notes}` : ""}</p>
      </div>
      <button class="remove-button" type="button" data-id="${appointment.id}" aria-label="Remover agendamento de ${appointment.client}">×</button>
    </article>
  `).join("");
}

function resetForm() {
  bookingForm.reset();
  bookingDate.value = todayISO();
  renderAppointments();
}

function handleSubmit(event) {
  event.preventDefault();

  const client = document.querySelector("#clientName").value.trim();
  const phone = document.querySelector("#clientPhone").value.trim();
  const service = getService(serviceSelect.value);
  const barber = getBarber(barberSelect.value);
  const date = bookingDate.value;
  const time = bookingTime.value;
  const notes = document.querySelector("#bookingNotes").value.trim();

  const conflict = appointments.some(appointment =>
    appointment.date === date &&
    appointment.time === time &&
    appointment.barberId === barber.id
  );

  if (conflict) {
    showToast("Esse barbeiro já tem atendimento neste horário.");
    return;
  }

  appointments.push({
    id: crypto.randomUUID(),
    client,
    phone,
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
  showToast("Agendamento confirmado.");
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

renderServices();
renderBarbers();
renderTimes();
bookingDate.min = todayISO();
bookingDate.value = todayISO();
renderAppointments();

bookingForm.addEventListener("submit", handleSubmit);
bookingDate.addEventListener("change", renderAppointments);
appointmentList.addEventListener("click", handleListClick);
clearSchedule.addEventListener("click", handleClear);
