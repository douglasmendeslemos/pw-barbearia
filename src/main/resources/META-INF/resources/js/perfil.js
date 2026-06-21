// Array para armazenar histórico
let historico = [];

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
