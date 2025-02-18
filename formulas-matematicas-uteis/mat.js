
// Fórmulas matemáticas úteis para programas ➕➖📈📋📊


//Área do círculo --------------------------------------------------------------------
var areaCirculo = (raio) => Math.PI * (raio * raio); 
/*No exemplo abaixo basta descomentar a linha e substituir o número pelo valor do raio*/
//console.log(areaCirculo(3));

//Área do triângulo (fórmula de heron)-------------------------------------------------
var areaTriangulo = (ladoA, ladoB,ladoC) => 
{
   //semiperímetro
   let p = (ladoA+ladoB+ladoC)/2;
   //raiz quadrada
   return Math.sqrt(p*(p-ladoA)*(p-ladoB)*(p-ladoC))
};
//console.log(areaTriangulo(7,9,14));

//Fatorial ---------------------------------------------------------------------------
var fatorial = (numero) => 
{
 let resultado = numero;
 for (let i = 1; i < numero; i++) {resultado *= i;}
 return resultado;
}
//console.log(fatorial(5));


//Média aritimética ------------------------------------------------------------------
var media = (array) => 
{
    let resposta = array.reduce(function(acumulador,valor){return (acumulador + valor);});
    return resposta/array.length;
}
//console.log(media([1,2,3,4,5,6]));

//Média ponderal ---------------------------------------------------------------------
var mediaPonderal = (valores,pesos) => 
{
 var resultado = 0;
 var dividendo = 0;
 var divisor = 0;
 for (const i in valores){dividendo += (valores[i] * pesos[i]);}
 for (const i of pesos) {divisor += i;}
 return dividendo/divisor;
}
//Adicone os valores dentro do 1º array e o peso de cada valor(vezes que se repete) dentro do 2º array
//console.log(mediaPonderal([8,9.2,7.9,8],[1,2,3,4]))

//Mínimo Divisor Comum (mmc)-------------------------------------------------------------------
var mmc = function(numero1, numero2) 
{
  var resto, a, b;
  a = numero1;
  b = numero2;

  do 
  {
    resto = a % b;
    a = b;
    b = resto;
  } 
    
  while (resto != 0);
  return (numero1 * numero2) / a;
}

//console.log(mmc(2,3));

//Números primos ---------------------------------------------------------------------
//Teorema de Fermat❤
let primos =(num)=>{
    //Aplicação do teorema 
    let fermat = (Math.pow(2,num-1))%num;
   //verificação do resultado do teorema com if ternário
    let teste = fermat === 1 ? console.log(num+" é primo!"):
    console.log(num+" não é primo");
 }
 //Digite o número desejado dentro da função seguinte
 //primos(19);

//Perímetro do círculo ----------------------------------------------------------------
var perimetroCirculo = (raio) => 2 * Math.PI * raio; 
/*No exemplo abaixo basta descomentar a linha e substituir o número pelo valor do raio*/
//console.log(perimetroCirculo(3));

//Porcentagem ------------------------------------------------------------------------
//Para saber quanto porcento um valor é em relação a  um total
var porcento = (valor,total) => (10 * valor)/(0.1 * total);
//Exemplo: 810 é quanto porcento de 4500?
//console.log(porcento(810,4500)+"%");


