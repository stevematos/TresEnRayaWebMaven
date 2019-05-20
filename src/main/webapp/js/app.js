/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var tictactoe = document.querySelector('.tictactoe-js');
var centinel = true;
var count = 0;
var podium = false;
var c1 = document.getElementById('c1');
var c2 = document.getElementById('c2');
var c3 = document.getElementById('c3');
var c4 = document.getElementById('c4');
var c5 = document.getElementById('c5');
var c6 = document.getElementById('c6');
var c7 = document.getElementById('c7');
var c8 = document.getElementById('c8');
var c9 = document.getElementById('c9');
let type = document.getElementById('select-type');
var type_algoritmo = type.options[type.selectedIndex].value

window.onload = function() {
  tictactoe.addEventListener('click', addX);
  document.getElementById('btn-reset').addEventListener('click', reset);
  document.getElementById('btn-automatico').addEventListener('click', jugadaIA);
};

function addX(event) {
  // Colocamos la 'X' u 'O' de acuerdo al turno
  if (event.target.matches('td') && event.target.textContent === '') {
    if (centinel) {
      event.target.textContent = 'X';
      centinel = false;
      count++;
    } else {
      event.target.textContent = 'O';
      centinel = true;
      count++;
    }
  }
  verificar()

  data=[c1.textContent,c2.textContent,c3.textContent,
    c4.textContent,c5.textContent,c6.textContent,
    c7.textContent,c8.textContent,c9.textContent];
    
  let xhr = new XMLHttpRequest();
  xhr.open('POST', '/ProyectoTresEnRayaAppMaven/servletTresEnRaya');
  xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
  xhr.onload = function() {
    console.log(xhr)
    if(xhr.readyState === 4) {
      if(xhr.status === 200) {
        respuesta =  xhr.responseText.split(";")
        document.getElementById('respuesta').innerHTML = respuesta[1];
      } else {
        alert('ERROR: Servlet no respondio como debia')
      }
    }
  }

  json = JSON.stringify(
    {
      data:data,
      auto:false,
      reset: false,
      type:type_algoritmo
    }
  )

  xhr.send(json);
};

function verificar(){
  if (count >= 5 && count <= 9) {
    toPlay();
  }

  if (count >= 9 && podium === false) {
    document.getElementById('winner').innerHTML = 'DEAD HEAT';
  }
}

function toPlay() {
  if ((c1.textContent === 'X' && c2.textContent === 'X' && c3.textContent === 'X') ||
 	 (c4.textContent === 'X' && c5.textContent === 'X' && c6.textContent === 'X') ||
 	 (c7.textContent === 'X' && c8.textContent === 'X' && c9.textContent === 'X') ||
 	 (c1.textContent === 'X' && c4.textContent === 'X' && c7.textContent === 'X') ||
      (c2.textContent === 'X' && c5.textContent === 'X' && c8.textContent === 'X') ||
      (c3.textContent === 'X' && c6.textContent === 'X' && c9.textContent === 'X') ||
      (c1.textContent === 'X' && c5.textContent === 'X' && c9.textContent === 'X') ||
      (c3.textContent === 'X' && c5.textContent === 'X' && c7.textContent === 'X')) {
 	 document.getElementById('winner').innerHTML = 'GANA: X ';
 	 count = 10;
    podium = true;
    // Declaramos todas las posibles situaciones en la que ganaria O
  } else if ((c1.textContent === 'O' && c2.textContent === 'O' && c3.textContent === 'O') ||
 	 (c4.textContent === 'O' && c5.textContent === 'O' && c6.textContent === 'O') ||
 	 (c7.textContent === 'O' && c8.textContent === 'O' && c9.textContent === 'O') ||
 	 (c1.textContent === 'O' && c4.textContent === 'O' && c7.textContent === 'O') ||
      (c2.textContent === 'O' && c5.textContent === 'O' && c8.textContent === 'O') ||
      (c3.textContent === 'O' && c6.textContent === 'O' && c9.textContent === 'O') ||
      (c1.textContent === 'O' && c5.textContent === 'O' && c9.textContent === 'O') ||
      (c3.textContent === 'O' && c5.textContent === 'O' && c7.textContent === 'O')) {
    document.getElementById('winner').innerHTML = 'GANA: O ';
    count = 10;
    podium = true;
  } else {

  }
}


function reset() {
  window.location.reload();
  let xhr = new XMLHttpRequest();
  xhr.open('POST', '/ProyectoTresEnRayaAppMaven/servletTresEnRaya');
  xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
  xhr.onload = function() {
    console.log(xhr)
    if(xhr.readyState === 4) {
      if(xhr.status === 200) {
        respuesta =  xhr.responseText.split(";")
        document.getElementById('respuesta').innerHTML = respuesta[1];
      } else {
        alert('ERROR: Servlet no respondio como debia')
      }
    }
  }
  json = JSON.stringify(
    {
      data:[],
      auto:false,
      reset: true,
      type:type_algoritmo
    }
  )
  xhr.send(json);
}

function jugadaIA(){
  data=[c1.textContent,c2.textContent,c3.textContent,
        c4.textContent,c5.textContent,c6.textContent,
        c7.textContent,c8.textContent,c9.textContent];
  let xhr = new XMLHttpRequest();
  xhr.open('POST', '/ProyectoTresEnRayaAppMaven/servletTresEnRaya');
  xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
  xhr.onload = function() {
    console.log(xhr)
    if(xhr.readyState === 4) {
      if(xhr.status === 200) {
        respuesta =  xhr.responseText.split(";")
        valores = respuesta[0].slice(1,-1).split(",").map(valor => {
          let aux = valor.trim()
          if (aux==='-'){
              return ''
          }else
              return aux
        })
        c1.textContent = valores[0]
        c2.textContent = valores[1]
        c3.textContent = valores[2]
        c4.textContent = valores[3]
        c5.textContent = valores[4]
        c6.textContent = valores[5]
        c7.textContent = valores[6]
        c8.textContent = valores[7]
        c9.textContent = valores[8]
        count++;
        centinel= !centinel
        document.getElementById('respuesta').innerHTML = respuesta[1];
        verificar()
      } else {
        alert('ERROR: Servlet no respondio como debia')
      }
    }
  }

  console.log(type_algoritmo)

  let turno = (centinel) ? 'X':'O'; 
  json = JSON.stringify(
    {
      data:data,
      auto:true,
      reset: false,
      type:type_algoritmo
    }
  )

  xhr.send(json);
}