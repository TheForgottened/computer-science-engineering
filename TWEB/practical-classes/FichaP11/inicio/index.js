/**************************************************
 * Javascript - Ficha 11
 * ************************************************/

init();

document.getElementById('btn-selecionaimagem').addEventListener('click', selection);
document.getElementById('btn-next').addEventListener('click', next);
document.getElementById('btn-previous').addEventListener('click', previous);
document.getElementById('btn-reset').addEventListener('click', init);

for (let i = 1; i < 9; i++){
    document.getElementById("animal-" + i).addEventListener('click', fclick);
    document.getElementById("animal-" + i).addEventListener('mouseover', fmover);
    document.getElementById("animal-" + i).addEventListener('mouseout', fmout);
}

var currentAnimal = 0;
var nameAnimals = ['Abelha', 'Peixe', 'Golfinho', 'Macaco', 'Polvo', 'Tartaruga', 'Elefante', 'Fantasma'];

function init() {
    let previous = document.getElementById('btn-previous');
    let next = document.getElementById('btn-next');
    let reset = document.getElementById('btn-reset');
    let select = document.getElementById('btn-selecionaimagem');

    document.getElementById('animal-box').style.visibility = "hidden";
    document.querySelector('.active').classList.remove('active');

    for (let i = 1; i < 9; i++){
        let id = "animal-" + i;
        document.getElementById(id).classList.remove('active');
    }

    previous.disabled = true;
    next.disabled = true;
    reset.disabled = true;
    select.disabled = false;

    next.classList.remove('border-active');
    previous.classList.remove('border-active');
    reset.classList.remove('border-active');
}

function selection(){
    let previous = document.getElementById('btn-previous');
    let next = document.getElementById('btn-next');
    let reset = document.getElementById('btn-reset');
    let animalbox = document.getElementById('animal-box');

    this.disabled = true;
    
    previous.disabled = false;
    next.disabled = false;
    reset.disabled = false;

    next.classList.add('border-active');
    previous.classList.add('border-active');
    
    document.getElementById('animal-1').classList.add('active');

    animalbox.style.visibility = "visible";
    animalbox.textContent = "Abelha"

    currentAnimal = 1;
}

function next(){
    let animalbox = document.getElementById('animal-box');

    let id1 = "animal-" + currentAnimal;

    document.getElementById(id1).classList.remove('active');

    if (currentAnimal == 8){
        currentAnimal = 1;
    }
    else{
        currentAnimal++;
    }

    let id2 = "animal-" + currentAnimal;

    document.getElementById(id2).classList.add('active');

    animalbox.textContent = nameAnimals[currentAnimal - 1];
}

function previous(){
    let animalbox = document.getElementById('animal-box');

    let id1 = "animal-" + currentAnimal;

    document.getElementById(id1).classList.remove('active');

    if (currentAnimal == 1){
        currentAnimal = 8;
    }
    else{
        currentAnimal--;
    }

    let id2 = "animal-" + currentAnimal;

    document.getElementById(id2).classList.add('active');

    animalbox.textContent = nameAnimals[currentAnimal - 1];
}

function fclick(){
    let animalbox = document.getElementById('animal-box');

    for (let i = 1; i < 9; i++){
        let id = "animal-" + i;
        document.getElementById(id).classList.remove('active');
    }

    if(document.getElementById('btn-selecionaimagem').disabled == true){
        currentAnimal = this.id.slice(7);
        
        document.getElementById("animal-" + currentAnimal).classList.add('active');
        animalbox.textContent = nameAnimals[currentAnimal - 1];
    }
}

function fmover(){
    if(document.getElementById('btn-selecionaimagem').disabled == true){
        this.classList.add('border-active');
    }
}

function fmout(){
    if(document.getElementById('btn-selecionaimagem').disabled == true){
        this.classList.remove('border-active');
    }
}