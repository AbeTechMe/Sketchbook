//Digite a baixo os valores coletados -------------------------------------------
let valores = [8, 4, 6, 9, 10, 5];

//Ordenando os dados coletados (ROL)---------------------------------------------

//Ordena  o array numericamente de forma crescente.
function sortFunction(a, b) {
  return a - b;
}
valores.sort(sortFunction);

//Imprime a quantidade de dados
const N = valores.length;
console.log(`Rol dos ${N} dados: ${valores}`);
console.log("");

//Frequência dos dados (f)---------------------------------------------------------
let f = [];
let valor = [];
let freq = [];
let val = [];
let contador = 1;

for (let key = 0; key < valores.length; key++) {
  if (valores[key] == valores[key + 1]) {
    contador++;
  } else {
    f.push(contador); //será apagada em outra função
    freq.push(contador);
    valor.push(valores[key]); //será apagada em outra função
    val.push(valores[key]);
    contador = 1;
  }
}

//console.log(freq);
//console.log(val);

//Frequência absoluta(fA)--------------------------------------------------------------
let fA = [f[0]];

for (let iterator = 1; iterator < f.length; iterator++) {
  let valor = f[iterator] + fA[iterator - 1];
  fA.push(valor);
  valor = 0;
}
//console.log(fA);

//Frequência relativa(fR)------------------------------------------------------------
let fR = f.map(function (item) {
  return item / valores.length;
});
//console.log(fR);

//Frequência relativa em percentual(fR%)---------------------------------------------
let fRP = fR.map(function (item) {
  return item * 100;
});
//console.log(fR);

//Imprimindo a tabela----------------------------------------------------------------
let dados = [];
for (const key in valor) {
  dados.push([valor[key], f[key], fA[key], fR[key], fRP[key] + "%"]);
}
console.log("----------Frequências dos dados-------------");
console.log(
  "|  indice |Val | f |f A|        f Rel        |         fR              |"
);
console.table(dados);

//                                     ---FREQUÊNCIA POR CLASSES---
//Amplitude Total (At)-----------------------------------------------------------------
let aT = valores[valores.length - 1] - valores[0];
//console.log("Amplitude total:"+aT);

//Número de classes (nC)---------------------------------------------------------------
// O n° de classes foi obtido com a (√ da amostra + método Sturges)/2
let nC = Math.round(
  (Math.sqrt(valores.length) + (1 + 3.3 * Math.log10(20))) / 2
);
//console.log("Números de classes para f por classe:"+nC);

//Amplitude da classe(A)---------------------------------------------------------------
let A = Math.round(aT / nC);
//console.log("Amplitude de cada classe:"+A);

//Gerando as classes-------------------------------------------------------------------
//limite de cada classe
let ClassesLimites = [];
let limites = valor[0];
for (let i = 0; i < nC; i++) {
  limites += A;
  ClassesLimites.push(limites);
}
//console.log("Limites das classes:"+ClassesLimites);

//Frequência das classes (Classes)-----------------------------------------------------------------
let Classes = [];
let soma = 0;

for (let i = 0; i < ClassesLimites.length; i++) {
  for (let j = 0; j < fA.length; j++) {
    if (valor[0] < ClassesLimites[i]) {
      soma += f[0];
      f.splice(0, 1);
      valor.splice(0, 1);
    }
  }
  Classes.push(soma);
  soma = 0;
}

//Frequência absoluta das classes (ClassesAbs)--------------------------------------------------------------
let ClassesAbs = [Classes[0]];
for (let iterator = 1; iterator < Classes.length; iterator++) {
  let valor = Classes[iterator] + ClassesAbs[iterator - 1];
  ClassesAbs.push(valor);
  valor = 0;
}
//console.log(ClassesAbs);

//Frequência relativa das classes(ClassesRel)----------------------------------------------------------
let ClassesRel = Classes.map(function (item) {
  return item / valores.length;
});
//console.log(ClassesRel);

//Frequência relativa em percentual(ClassesfRP)----------------------------------------------------
let ClassesfRP = ClassesRel.map(function (item) {
  return item * 100;
});
//console.log(ClassesfRP);

//Ponto Médio das classes(Pm)-------------------------------------------------------------------------
let Pm = ClassesLimites.map(function (item) {
  return (item - A + item) / 2;
});
//console.log(Pm);

let tabelaClasses = [];
for (const key in Classes) {
  tabelaClasses.push([
    `${ClassesLimites[key] - A}|--${ClassesLimites[key]}`,
    Classes[key],
    ClassesAbs[key],
    ClassesRel[key],
    ClassesfRP[key] + "%",
    Pm[key],
  ]);
}

//Imprimindo classes
console.log("----------Frequências e ponto médio por classes-------------");
console.log(
  "|  indice |  Classes  | f |f A|          fR         |          fR           | Pm |"
);
console.table(tabelaClasses);

//                                ----MEDIDAS DE POSIÇÃO----
console.log();
console.log("---Medidas de posição dos dados---");
console.log();

//Média simples---------------------------------------------------------------------
let media = 0;
valores.forEach((element) => {
  media += element;
});
media /= valores.length;
console.log("---------------------------------");
console.log("média simples(X): " + media);

//Média podenrada---------------------------------------------------------------------

let mediaPonderada;
contador = 0;

for (const key in freq) {
  contador += val[key] * freq[key];
}
mediaPonderada = contador / valores.length;
console.log("---------------------------------");
console.log("média ponderada: " + mediaPonderada);

//Média podenrada em classes------------------------------------------------------------------
//Usando o ponto médio (pm)
/*contador = 0;
let mediaPonderadaClasses;
for (const key in Pm) {contador += (Pm[key] * Classes[key]);}
mediaPonderadaClasses = contador/valores.length;
console.log("---------------------------------");
console.log("média ponderada: "+mediaPonderadaClasses);
console.log("---------------------------------");*/

//mediana-----------------------------------------------------------------------------------------
//Em dados não agrupados

let valoresClear = valores;
let mediana;

if (N % 2 == 0) {
  mediana = (valoresClear[N / 2 - 1] + valoresClear[N / 2]) / 2; // O -1 é por ser index
  console.log("---------------------------------");
  console.log("Mediana: " + mediana);
  console.log("---------------------------------");
} else {
  for (let key = 0; key < N; key++) {
    if (valoresClear.length > 1) {
      valoresClear.splice(0, 1);
    }
    if (valoresClear.length > 1) {
      valoresClear.splice(valoresClear.length - 1, 1);
    }
  }
  mediana = valoresClear[0];
  console.log("---------------------------------");
  console.log("Mediana:" + mediana);
  console.log("---------------------------------");
}

//Moda---------------------------------------------------------------------------

let maiorF = [0];
let moda = [];

for (const key in freq) {
  //Pega a maior frequência
  if (maiorF[0] < freq[key]) {
    maiorF[0] = freq[key];
  }
}

for (const key in freq) {
  //Pega o(s) valor(es) de maior(es) frequência(s)
  if (maiorF[0] == freq[key]) {
    moda.push(val[key]);
  }
}

//verifica se é amodal
if (moda.length === val.length) {
  console.log("moda: distribuição AMODAL");
  console.log("---------------------------------");
} else {
  //Imprimindo a moda
  console.log("---------------------------------");
  console.log("moda: " + moda);
  console.log("---------------------------------");
}

//Medidas de dispersão
console.log();
console.log("---Medidas dedispersão---");
console.log();

//Amplitude total (Limite superior - Limite Inferior)-----------------------------
console.log("---------------------------------");
console.log("Amplitude total: " + aT);

//Desvio médio-------------------------------------------------------------------
let dM = 0;
for (const index in val) {
  dM += Math.abs(val[index] - media) * freq[index];
}
dM /= valores.length;

console.log("---------------------------------");
console.log("Desvio médio: " + dM);

//Variância---------------------------------------------------------------------
let variancia = 0;
for (const index in val) {
  variancia += (val[index] - media) ** 2 * freq[index];
}
//variancia /= valores.length;

console.log("---------------------------------");
console.log("Variância (população): " + variancia / N);
console.log("---------------------------------");
console.log("Variância (amostra): " + variancia / (N - 1));

//Desvio padrão-----------------------------------------------------------------
let DesvioPadraoAmostra = Math.sqrt(variancia / (N - 1));
let DesvioPadraoPopolacao = Math.sqrt(variancia / N);
console.log("---------------------------------");
console.log("Desvio padrão (população): " + DesvioPadraoPopolacao);
console.log("---------------------------------");
console.log("Desvio padrão (amostra): " + DesvioPadraoAmostra);
console.log("---------------------------------");

//                             ---MEDIDAS DE ASSIMETRIA---
console.log();
console.log("---Medidas de assimetria---");
console.log();

if (moda > 0) {
  //1° coeficiente de Pearson  = (media - moda)/Desvio Padrão
  let coefPearson1 = (mediaPonderada - moda) / DesvioPadraoAmostra;
  //verifica a simetria da amostra
  let resposta =
    (coefPearson1 == 0 && "Distribuição simétrica") ||
    (coefPearson1 > 0 && "Distribuição assimétrica a direita") ||
    (coefPearson1 < 0 && "Distribuição assimétrica a esquerda");
 
  console.log("---------------------------------");
  console.log(`${resposta}  (coefP1:${coefPearson1})`);
} 
else {
  //2° coeficiente de Pearson = (3 * (média - mediana))/Desvio Padrão
  let coefPearson2 = (3 * (mediaPonderada - mediana)) / DesvioPadraoAmostra;

  //verifica a simetria da amostra
  let resposta =
    (coefPearson2 == 0 && "Distribuição simétrica") ||
    (coefPearson2 > 0 && "Distribuição assimétrica a direita") ||
    (coefPearson2 < 0 && "Distribuição assimétrica a esquerda");
  
  console.log("---------------------------------");
  console.log(`${resposta}  (coefP2:${coefPearson2})`);
}

//Curtose--------------------------------------------------------------------------

//Cálculo dos quartis
//primeiro quartil
let valor1 = (N * 1) / 4;
let q1 = 0;
for (const key in ClassesAbs) {
  if (valor1 == ClassesAbs[key] || valor1 < ClassesAbs[key]) {
    let freqAbsAnterior = ClassesAbs[key - 1];
    if (!freqAbsAnterior) {
      freqAbsAnterior = 0;
    }
    q1 = ClassesLimites[key] + ((valor1 - freqAbsAnterior) / Classes[key]) * A;
    break;
  }
}
//console.log("primeiro quartil:"+q1);

//segundo quartil
valor1 = (N * 3) / 4;
let q3 = 0;
for (const key in ClassesAbs) {
  if (valor1 == ClassesAbs[key] || valor1 < ClassesAbs[key]) {
    let freqAbsAnterior = ClassesAbs[key - 1];
    if (!freqAbsAnterior) {
      freqAbsAnterior = 0;
    }
    q3 = ClassesLimites[key] + ((valor1 - freqAbsAnterior) / Classes[key]) * A;
    break;
  }
}
//console.log("segundo quartil:"+q3);

//Cálculo dos percentis

//primeiro percentil
valor1 = (N * 10) / 100;
let p10 = 0;
for (const key in ClassesAbs) {
  if (valor1 == ClassesAbs[key] || valor1 < ClassesAbs[key]) {
    let freqAbsAnterior = ClassesAbs[key - 1];
    if (!freqAbsAnterior) {
      freqAbsAnterior = 0;
    }
    p10 = ClassesLimites[key] + ((valor1 - freqAbsAnterior) / Classes[key]) * A;
    break;
  }
}
//console.log("primeiro percentil:"+p10);

//segundo percentil
valor1 = (N * 90) / 100;
let p90 = 0;
for (const key in ClassesAbs) {
  if (valor1 == ClassesAbs[key] || valor1 < ClassesAbs[key]) {
    let freqAbsAnterior = ClassesAbs[key - 1];
    if (!freqAbsAnterior) {
      freqAbsAnterior = 0;
    }
    p90 = ClassesLimites[key] + ((valor1 - freqAbsAnterior) / Classes[key]) * A;
    break;
  }
}
//console.log("segundo percentil:"+p90);

//curtose
let k = (q3 - q1) / (2 * (p90 - p10));
//console.log("valor da curtose: "+k);

switch (true) {
  case k == 0.263:
    console.log("---------------------------------");
    console.log(`Curva normal - mesocúrtica (k:${k})`);
    console.log("---------------------------------");
    break;

  case k > 0.263:
    console.log("---------------------------------");
    console.log(`Curva achatada - platicúrtica (k:${k})`);
    console.log("---------------------------------");
    break;

  case k < 0.263:
    console.log("---------------------------------");
    console.log(`Curva alongada - leptocúrtica (k:${k})`);
    console.log("---------------------------------");
    break;
}
//:)
