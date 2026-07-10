// Função para carregar os dados e construir os gráficos
async function carregarDadosDashboard() {
    try {
        const resposta = await fetch("/dashboard/dados");
        if (!resposta.ok) {
            console.error("Erro ao buscar dados do dashboard.");
            return;
        }

        const dados = await resposta.json();

        // 1. Preencher KPIs
        document.getElementById("kpiClientes").textContent = dados.totalClientes.toLocaleString();
        document.getElementById("kpiBarbeiros").textContent = dados.totalBarbeiros.toLocaleString();
        document.getElementById("kpiAgendamentos").textContent = dados.totalAgendamentos.toLocaleString();
        document.getElementById("kpiFaturamento").textContent = dados.faturamentoTotal.toLocaleString("pt-BR", {
            style: "currency",
            currency: "BRL"
        });

        // Cores elegantes do tema Barbearia
        const coresBarbearia = [
            "#800000", // Vermelho Borgonha / Brand
            "#B8860B", // Dourado escuro / Accent
            "#8B4513", // Marrom sela
            "#2F4F4F", // Cinza ardósia escuro
            "#006400", // Verde floresta
            "#4B0082"  // Índigo
        ];

        // 2. Gráfico de Faturamento Diário (Linha)
        const datasDia = dados.faturamentoPorDia.map(d => formatarData(d.data));
        const faturamentosDia = dados.faturamentoPorDia.map(d => d.faturamento);
        
        new Chart(document.getElementById("chartFaturamentoDia"), {
            type: "line",
            data: {
                labels: datasDia,
                datasets: [{
                    label: "Faturamento Diário (R$)",
                    data: faturamentosDia,
                    borderColor: "#800000",
                    backgroundColor: "rgba(128, 0, 0, 0.1)",
                    borderWidth: 3,
                    fill: true,
                    tension: 0.3
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function(value) {
                                return 'R$ ' + value.toLocaleString('pt-BR');
                            }
                        }
                    }
                }
            }
        });

        // 3. Gráfico de Faturamento por Barbeiro (Rosca/Doughnut)
        const nomesBarbeiros = dados.faturamentoPorBarbeiro.map(b => b.barbeiro);
        const faturamentosBarbeiros = dados.faturamentoPorBarbeiro.map(b => b.faturamento);

        new Chart(document.getElementById("chartFaturamentoBarber"), {
            type: "doughnut",
            data: {
                labels: nomesBarbeiros,
                datasets: [{
                    data: faturamentosBarbeiros,
                    backgroundColor: coresBarbearia,
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                let value = context.raw || 0;
                                return ' ' + context.label + ': R$ ' + value.toLocaleString('pt-BR', { minimumFractionDigits: 2 });
                            }
                        }
                    }
                }
            }
        });

        // 4. Gráfico de Agendamentos por Serviço (Barras Horizontais)
        const nomesServicos = dados.faturamentoPorServico.map(s => s.servico);
        const agendamentosServicos = dados.faturamentoPorServico.map(s => s.agendamentos);

        new Chart(document.getElementById("chartServicos"), {
            type: "bar",
            data: {
                labels: nomesServicos,
                datasets: [{
                    label: "Agendamentos",
                    data: agendamentosServicos,
                    backgroundColor: "#B8860B",
                    borderRadius: 6
                }]
            },
            options: {
                indexAxis: 'y',
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false }
                },
                scales: {
                    x: { beginAtZero: true, ticks: { stepSize: 1 } }
                }
            }
        });

        // 5. Gráfico de Quantidade de Agendamentos por Barbeiro (Barras Verticais)
        const nomesBarbeirosAgendamentos = dados.faturamentoPorBarbeiro.map(b => b.barbeiro);
        const quantidadeAgendamentos = dados.faturamentoPorBarbeiro.map(b => b.agendamentos);

        new Chart(document.getElementById("chartAgendamentosBarber"), {
            type: "bar",
            data: {
                labels: nomesBarbeirosAgendamentos,
                datasets: [{
                    label: "Quantidade de Agendamentos",
                    data: quantidadeAgendamentos,
                    backgroundColor: "#2F4F4F",
                    borderRadius: 6
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false }
                },
                scales: {
                    y: { beginAtZero: true, ticks: { stepSize: 1 } }
                }
            }
        });

    } catch (erro) {
        console.error("Erro ao carregar dados do dashboard:", erro);
    }
}

// Auxiliar para formatar data (AAAA-MM-DD -> DD/MM/AAAA)
function formatarData(dataString) {
    if (!dataString) return "";
    const partes = dataString.split("-");
    if (partes.length === 3) {
        return `${partes[2]}/${partes[1]}/${partes[0].substring(2)}`; // Ex: 08/07/26
    }
    return dataString;
}

// Inicializa a carga
carregarDadosDashboard();
