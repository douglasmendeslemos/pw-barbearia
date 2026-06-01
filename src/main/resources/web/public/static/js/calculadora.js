// Array para armazenar histórico
let historico = [];

let protocolo = "http://"
let domain = "localhost:8080"
let path = "/calculo/save"
let url = protocolo + domain + path

function obterNumeros() {
    let nome = Number(document.getElementById("nome").value);
    let num2 = Number(document.getElementById("num2").value);
    return { num1: num1, num2 };
}

function mostrarResultado(valor) {
    document.getElementById("resultado").textContent = valor;
}

function somar() {
    let numeros = obterNumeros();
    let resultado = numeros.num1 + numeros.num2;
    mostrarResultado(resultado);
    registrarCalculo(numeros.num1, "+", numeros.num2, resultado);
}

function subtrair() {
    let numeros = obterNumeros();
    let resultado = numeros.num1 - numeros.num2;
    mostrarResultado(resultado);
    registrarCalculo(numeros.num1, "-", numeros.num2, resultado);
}

function multiplicar() {
    let numeros = obterNumeros();
    let resultado = numeros.num1 * numeros.num2;
    mostrarResultado(resultado);
    registrarCalculo(numeros.num1, "*", numeros.num2, resultado);
}

function dividir() {
    let numeros = obterNumeros();

    if (numeros.num2 === 0) {
        mostrarResultado("Erro: divisão por zero");
        return;
    }

    let resultado = numeros.num1 / numeros.num2;
    mostrarResultado(resultado);
    registrarCalculo(numeros.num1, "/", numeros.num2, resultado);
}

function salvarCalculo(calculo){

    // Requisição
    fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(calculo)
    })
     // Resposta por callback
    .then(function (response) {
        if (response.status === 200)
            recuperarCalculos();
        // return response.text();
        // callback();
    })
    .then(function (data){
        console.log(data);
    })
    //Tratamento de erro
    .catch(function (erro) {
        console.error("Erro ao enviar:", erro);
    });
}

function recuperarCalculos(){

    fetch(url, {
        method: "GET"
        // Resposta por callback
        })
        .then(function (response) {
            return response.json();
            // callback();
        })
        .then(function (data){
            historico = data;
            atualizarTabela();
        })
        //Tratamento de erro
        .catch(function (erro) {
            console.error("Erro ao enviar:", erro);
        });
}


function registrarCalculo(num1, operacao, num2, resultado) {

    let calculo = {
        numero1: num1,
        operacao: operacao,
        numero2: num2,
        resultado: resultado
    };

    salvarCalculo(calculo);

    // historico.push(calculo);
    // atualizarTabela();
}

function atualizarTabela() {

    let tabela = document.getElementById("tabelaHistorico");

    tabela.innerHTML = "";

    for (let i = 0; i < historico.length; i++) {

        let linha = "<tr>" +
            "<td>" + historico[i].numero1 + "</td>" +
            "<td>" + historico[i].operacao + "</td>" +
            "<td>" + historico[i].numero2 + "</td>" +
            "<td>" + historico[i].resultado + "</td>" +
            "</tr>";

        tabela.innerHTML += linha;

        console.log(tabela.innerHTML);
    }
}

window.onload = recuperarCalculos;