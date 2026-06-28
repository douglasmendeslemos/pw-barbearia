
async function fazerLogin() {
    console.log("entrei na função login")
    const emailDigitado = document.getElementById('email').value
    const senhaDigitada = document.getElementById('senha').value

    const loginRequestDTO = {
        email: emailDigitado,
        senha: senhaDigitada
    };

    try {
        const resposta = await fetch('/login/auth', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(loginRequestDTO)
        });
        console.log(resposta.status)
        if (!resposta.ok) {
            alert("Credenciais inválidas");
            return;
        }
        const dados = await resposta.json();
        sessionStorage.setItem('nome', dados.nome);
        sessionStorage.setItem('perfil', dados.perfil);

        window.location.href = "/"
    } catch (erro) {
        console.error("Erro ao conectar com o servidor", erro);
        alert("O servidor está fora do ar.")
    }
}