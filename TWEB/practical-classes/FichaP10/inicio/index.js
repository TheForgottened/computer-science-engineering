/**************************************************
 * Javascript - Ficha 10
 * ************************************************/


document.getElementById('btn-alert2').addEventListener('click', wrapper);

document.getElementById('btn-titulo').addEventListener('click', mudar);

document.getElementById('btn-titulo').addEventListener('mouseover', mudar2);

document.getElementById('btn-titulo').addEventListener('mouseout', mudarR);

document.querySelector('.btn-border').addEventListener('click', function(){
    if(animals.classList.contains('border-active'))
    {
        animals.classList.remove('border-active');
        animals.style.backgroundColor='white';
    }
    else
    {
        animals.classList.add('border-active');
        animals.style.backgroundColor='#FF000022';
    }
});

var animals =  document.querySelector('.panel-animals')

function alerta(){
    alert('TEXTO RANDOM CRL');
}

function wrapper(){
    alerta();
}

function mudar(){
    document.querySelector('.title').textContent='TECNOLOGIAS WEB - JAVASCRIPT';
}

function mudar2(){
    document.querySelector('.title').innerHTML='<h4>INTRODUÇÃO AO JAVASCRIPT</h4>';
}

function mudarR(){
    document.querySelector('.title').textContent='INTRODUÇÃO AO JAVASCRIPT';
}