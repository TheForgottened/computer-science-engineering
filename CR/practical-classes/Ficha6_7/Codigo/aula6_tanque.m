function [fis_tanque] = aula6_tanque()
    % Controlador da água de um tanque

    % PASSO 1: crie a estrutura FIS
    fis_tanque = newfis('tanque');

    % PASSO 2: criar variáveis linguísticas
    fis_tanque = addvar(fis_tanque, 'input', 'nivel', [-1 1]);
    fis_tanque = addvar(fis_tanque, 'input', 'fluxo', [-0.4 0.4]);
    fis_tanque = addvar(fis_tanque, 'output', 'valvula', [-1 1]);

    % PASSO 3: funções de pertençaa para cada variável criada anteriormente
    fis_tanque = addmf(fis_tanque, 'input', 1, 'alto', 'gaussmf', [0.275 -1]);
    fis_tanque = addmf(fis_tanque, 'input', 1, 'ok', 'gaussmf', [0.275 0]);
    fis_tanque = addmf(fis_tanque, 'input', 1, 'fraco', 'gaussmf',  [0.275 1]);

    fis_tanque = addmf(fis_tanque, 'input', 2, 'negativo', 'gaussmf', [0.025 -0.1]);
    fis_tanque = addmf(fis_tanque, 'input', 2, 'nulo', 'gaussmf', [0.025 0]);
    fis_tanque = addmf(fis_tanque, 'input', 2, 'positivo', 'gaussmf', [0.025 0.1]);

    fis_tanque = addmf(fis_tanque, 'output', 1, 'fecha-rapido', 'trimf', [-1 -0.9 -0.8]);
    fis_tanque = addmf(fis_tanque, 'output', 1, 'fecha-devagar', 'trimf', [-0.6 -0.5 -0.4]);
    fis_tanque = addmf(fis_tanque, 'output', 1, 'nada', 'trimf', [-0.1 0 0.1]);
    fis_tanque = addmf(fis_tanque, 'output', 1, 'abre-devagar', 'trimf', [0.2 0.3 0.4]);
    fis_tanque = addmf(fis_tanque, 'output', 1, 'abre-rapido', 'trimf', [0.8 0.9 1]);

    % Se o nível da água estiver bom, não ajuste a válvula.
    % Se o nível da água estiver baixo, abra a válvula rapidamente.
    % Se o nível da água estiver alto, feche a válvula rapidamente.
    % Se o nível da água estiver bom e aumentando (positivo), feche a válvula lentamente.
    % Se o nível da água estiver bom e diminuindo (negativo), abra a válvula lentamente.

    regras = [2 0 3 1 1; % input1 input2 output1 peso operador
              3 0 5 1 1;
              1 0 1 1 1;
              2 3 2 1 1;
              2 1 4 1 1];

    fis_tanque = addrule(fis_tanque, regras);
    entrada = [-1 0];
    out = evalfis(entrada, fis_tanque);
    fprintf('nivel = %f\nflixo = %f\nvalvula = %f\n', entrada(1), entrada(2), out);
end

