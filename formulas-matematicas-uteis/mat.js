
// Fórmulas matemáticas úteis para programas ➕➖📈📋📊



//Área do triângulo (fórmula de heron)-------------------------------------------------
// a fórmula de Heron pode ser usada para calcular a área de qualquer triângulo possível,
// desde que você conheça o comprimento dos três lados do triângulo
var areaTriangulo = (ladoA, ladoB, ladoC) => {

  //validando se as medidas formam um triângulo
  let triangulo =  (ladoA + ladoB > ladoC) && (ladoA + ladoC > ladoB) && (ladoB + ladoC > ladoA);

  if (triangulo) {
      //𝐴rea = √s(𝑠−𝑎)(𝑠−𝑏)(𝑠−𝑐)
      let s = (ladoA + ladoB + ladoC) / 2;
      return Math.sqrt(s * (s - ladoA) * (s - ladoB) * (s - ladoC));
  }

  return "As medidas fornecidas não formam um triângulo.";
};

// Exemplo de uso
//console.log(areaTriangulo(3, 4, 5)); // Saída: 6
//console.log(areaTriangulo(1, 1, 2)); // Saída: "As medidas fornecidas não formam um triângulo."

//Mínimo Divisor Comum (mmc)-------------------------------------------------------------------
var mmc = function(numeros) {

  // Função auxiliar para calcular o MMC de dois números
  var mmcDeDoisNumeros = function(numero1, numero2) {
      var resto, a, b;
      a = numero1;
      b = numero2;

      do {
          resto = a % b;
          a = b;
          b = resto;
      } while (resto != 0);

      return (numero1 * numero2) / a;
  };

  // Calcula o MMC de todos os números do array
  return numeros.reduce((mmcAtual, numero) => mmcDeDoisNumeros(mmcAtual, numero));
};
// Exemplo de uso
//console.log(mmc([12, 15, 20])); // Saída: 60
//console.log(mmc([4, 6, 8])); // Saída: 24

//Máximo Divisor Comum (mdc) Algoritmo de Euclides--------------------------------------------------
var mdc = function(numeros) {
  // Função auxiliar para calcular o MDC de dois números
  var mdcDeDoisNumeros = function(numero1, numero2) {
      var resto;
      while (numero2 != 0) {
          resto = numero1 % numero2;
          numero1 = numero2;
          numero2 = resto;
      }
      return numero1;
  };

  // Calcula o MDC de todos os números do array
  return numeros.reduce((mdcAtual, numero) => mdcDeDoisNumeros(mdcAtual, numero));
};
// Exemplo de uso
//console.log(mdc([12, 15, 20])); // Saída: 1
//console.log(mdc([16, 24, 32])); // Saída: 8


// Exemplo de uso
console.log(mdcDeDoisNumeros(56, 98)); // Saída: 14
console.log(mdcDeDoisNumeros(20, 30)); // Saída: 10


//Números primos - retorna se o número é primo ou não  (Teorema de Fermat) ---------------------------------------------------------------
// este teorema pode ter alguns falsos positivos
function fermat(numero, k = 5) {
  if (numero < 2) return false;

  let maxA = Math.min(numero - 2, 10000); // Define o intervalo adequado para 'a'
  k = Math.max(5, k); // Garante que o número mínimo de testes seja 5

  for (let i = 0; i < k; i++) {
      let a = Math.floor(Math.random() * (maxA - 2 + 1)) + 2; // Gera 'a' no intervalo [2, maxA]
      if (BigInt(a) ** BigInt(numero - 1) % BigInt(numero) !== 1n) {
          return false;
      }
  }
  return true;
}

// Exemplo de uso com número 17 e 8 testes
//console.log(fermat(17,8)); // Saída: true
// exemplo de uso com o número 18 e 5 testes
//console.log(fermat(18)); // Saída: false


// Todos números primos abaixo de um valor - Crivo de Eratóstenes
var crivoDeEratostenes = function(n) {
  let primos = Array(n + 1).fill(true);
  primos[0] = primos[1] = false;

  for (let p = 2; p * p <= n; p++) {
      if (primos[p]) {
          for (let i = p * p; i <= n; i += p) {
              primos[i] = false;
          }
      }
  }

  return primos.map((isPrime, index) => isPrime ? index : null).filter(n => n !== null);
};

// Exemplo de uso
console.log(crivoDeEratostenes(30)); // Saída: [2, 3, 5, 7, 11, 13, 17, 19, 23, 29]





