//informando o caminho path
let protocolo = "http://"
let domain = "localhost:8080"

function salvar(){
    let path = "/perfil/save"
    let url = protocolo + domain + path

    fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "text/plain"
        },
        body: document.getElementById("nome").value
    })
        // Resposta por callback
        .then(function (response) {
            if (response.status === 200)
                return response.json();
        })
        .then(function (data){
            console.log(data);
            alert(data);
        })
        //Tratamento de erro
        .catch(function (erro) {
            console.error("Erro ao enviar:", erro);
        });
}

async function fazerLogin(){
    const email = document.getElementById('email').value
    const senha = document.getElementById('senha').value

    const loginResquestDTO = {
        email: email,
        senha: senha
    };

    try{
        const resposta = await fetch('/auth/login', {
            method: 'POST', headers:{ 'Content-type': 'aplication/json'}, body: JSON.stringify(loginResquestDTO)
        } );if (!resposta.ok) {
            alert("Credenciais inválidas");
            return;
        }
        const dados = await resposta.json();
        sessionStorage.setItem('nome', dados.nome);
        sessionStorage.setItem('perfil', dados.perfil);

        window.location.href = "/dashboard"
    } catch (erro) {
        console.error("Erro ao conectar com o servidor", erro);
        alert("O servidor está fora do ar.")
    }
}