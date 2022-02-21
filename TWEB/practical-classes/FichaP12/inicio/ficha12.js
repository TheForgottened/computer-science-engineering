/**************************************************
 * Javascript - Ficha 12
 * ************************************************/

window.onload = function() {
  init();
};

function validaNome(fld) {
  var letters = /^[A-zÀ-ú]+$/;

  if (fld.value.trim().match(letters)) return true;

  return false;
}

function nota() {
  let panel = document.getElementById("notaTP-painel");
  let nota = document.getElementById("notaTP");

  if (this == document.getElementById("tpSim")) {
    panel.style.display = "initial";
  } else {
    panel.style.display = "none";
    nota.value = "0";
  }
}

function calculaNotaFinal(tp, exame) {
  let nota = (tp / 5) * 0.4 + exame * 0.6;

  return Math.round(nota);
}

function setStatus(nota, string = "") {
  let status = document.getElementById("painel-status");
  let situation = document.getElementById("situacao");
  let pnome = document.getElementById("pNome").value;
  let unome = document.getElementById("uNome").value;
  let final = document.getElementById("notaFinal");
  let msg = document.getElementById("msg");

  status.style.display = "initial";

  if (nota >= 10) {
    status.classList.add("aprovado");
    situation.textContent = "Aluno" + pnome + " " + unome + "aprovado!";
  } else {
    status.classList.add("reprovado");
    situation.textContent = "Aluno" + pnome + " " + unome + "reprovado!";
  }

  final.textContent = nota;

  if (string != "") {
    msg.style.display = "initial";
    msg.innerHTML = string;
  } else {
    msg.style.display = "none";
  }
}

function validaFormulario() {
  let yes = document.getElementById("tpSim");
  let nExame = document.getElementById("notaExame").value;
  let nTP = document.getElementById("notaTP").value;
  let nota = calculaNotaFinal(nTP, nExame);
  let pnome = document.getElementById("pNome");
  let unome = document.getElementById("uNome");
  // let form = document.getElementByTagName("form");
  let txt = "";

  if (form.checkValidity()) {
    // Especifique as validações aqui
    if (yes.checked && nTP.length < 1) {
      yes.setCustomValidity("Introduza a nota!");
      return;
    }

    if (!validaNome(pnome)) {
      pnome.setCustomValidity("Nome inválido! Especifique o primeiro nome.");
      return;
    }

    if (!validaNome(unome)) {
      unome.setCustomValidity("Nome inválido! Especifique o último nome.");
      return;
    }

    if (nExame < 7) {
      if (nota >= 10) nota = 9;
      txt = "Sem mínimos em exame!";
    }

    setStatus(nota, txt);
  } else {
    form.querySelectorAll(":invalid")[0].focus();
  }
}

function resetPanel() {
  document.getElementById("painel-status").style.display = "none";
  document.getElementById("msg").style.display = "none";

  document.getElementById("painel-status").classList.remove("aprovado");
  document.getElementById("painel-status").classList.remove("reprovado");

  document.getElementById("notaTP").setCustomValidity("");
  document.getElementById("pNome").setCustomValidity("");
  document.getElementById("uNome").setCustomValidity("");
}

function init() {
  let status = document.getElementById("painel-status");
  let panel = document.getElementById("notaTP-painel");
  let no = document.getElementById("tpNao");
  let yes = document.getElementById("tpSim");
  let submit = document.getElementById("btnCalcularNota");
  let nExame = document.getElementById("notaExame").value;
  let nTP = document.getElementById("notaTP").value;
  let pnome = document.getElementById("pNome").value;
  let unome = document.getElementById("uNome").value;
  let reset = document.getElementById("btnReset");

  status.style.display = "none";
  panel.style.display = "none";

  document.getElementById("pNome").focus();

  no.addEventListener("click", nota);
  yes.addEventListener("click", nota);
  reset.addEventListener("click", init);
  submit.addEventListener("click", validaFormulario);

  nExame = "";
  nTP = "";
  pnome = "";
  unome = "";

  resetPanel();
}
