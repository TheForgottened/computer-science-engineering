% Interface Geral para os PVI
% Aplica��o dos variados m�todos num�ricos em MatLab
% Autor: TheForgotten | https://github.com/TheForgottened
% Data: 07/04/2020

rmpath('./InterfaceGrafica');

clc;
clear;

primeiraVez = 1;
opcao = 1;

% inicializa��o de vari�veis
sF = [];
a = [];
b = [];
y0 = [];
n = [];
charIn = char(8712);

while (opcao ~= 9)
    clc;
    fprintf('M�TODOS NUM�RICOS PARA PVI\n\n');
    if (~primeiraVez)      
        fprintf('y'' = %s\nt %s [%.2f, %.2f]\ny(%.2f) = %d\nn = %d\n\n', sF, charIn, a, b, a, y0, n);
        fprintf('1. MUDAR PAR�METROS DO PVI\n');  
        fprintf('2. M�TODO DE EULER\n');   
        fprintf('3. M�TODO DE EULER MELHORADO\n');
        fprintf('4. M�TODO DE RK2\n');
        fprintf('5. M�TODO DE RK4\n');
        fprintf('6. M�TODO DE 2 PASSOS DE ADAMS\n');
        fprintf('7. M�TODO DA FUN��O ODE45\n');
        fprintf('8. APLICAR TODOS OS M�TODOS EM SIMULT�NEO\n');
        fprintf('9. TERMINAR\n');
        
        while 1
            opcao = input('Op��o: ', 's');

            [temp1, temp2] = str2num(opcao);
            
            if(temp2 && isscalar(temp1) && temp1 == floor(temp1) && temp1 >= 1 && temp1 <= 9)
                opcao = temp1;
                break;
            else
                fprintf(2, 'N�mero inv�lido!\n\n');
            end
        end
    else
        opcao = 1;
        primeiraVez = 0;
    end
    
    if (opcao ~= 1 && opcao ~= 9)
        sExata = dsolve(['Dy =', sF], ['y(', num2str(a), ') = ', num2str(y0)]);
        g = @(t) eval(vectorize(char(sExata)));
        h = (b - a) / n;
        t = a: h: b;
        yExata = g(t);
    end
    
    y = [];
    
    switch opcao
        case 1
            fprintf('Insira os par�metros do PVI:\n');
            
            while 1
                sF = input('\n�(t, y) = ','s');
                f = @(t,y) eval(vectorize(sF));
                
                try
                    syms t y;
                    fTeste = f(t, y);
                    break;
                catch me
                    fprintf(2, 'Fun��o inv�lida! Escreva uma fun��o em t e y.\n\n');
                end
            end
            
            a = F_PedirNumero('a');
            
            while 1
                b = F_PedirNumero('b');
                
                if(b > a)
                    break;
                else
                    fprintf(2, 'Introduza um n�mero maior que a = %d.\n\n', a);
                end
            end
                  
            while 1 
                n = F_PedirNumero('n');
                
                if(fix(n) == n)
                    break;
                else
                    fprintf(2, 'Introduza um n�mero natural!');
                end
            end
            
            y0 = F_PedirNumero('y0');
            y = [];
            
        case 2 % EULER
            y = NEuler(f, a, b, n, y0);
            s = 'Euler';
            
        case 3 % EULER MELHORADO
            y = NEuler_M(f, a, b, n, y0);
            s = 'Euler_M';
            
        case 4 % RK2
            y = NRK2(f, a, b, n, y0);
            s = 'RK2';
            
        case 5 % RK4
            y = NRK4(f, a, b, n, y0);
            s = 'RK4';
            
        case 6 % ADAMS
            y = NAdams(f, a, b, n, y0);
            s = 'Adams';
            
        case 7 % ODE45
            [~, y] = ode45(f, t, y0);
            y = y.';
            s = 'ODE45';
            
        case 8 % TODOS
            yEuler = NEuler(f, a, b, n, y0);
            yTemp = abs(yExata - yEuler);
            sTemp = 'Euler';
            F_ImprimirTabela(yEuler, yExata, yTemp, a, b, n, sTemp);
            
            yEuler_M = NEuler_M(f, a, b, n, y0);
            yTemp = abs(yExata - yEuler_M);
            sTemp = 'Euler_M';
            F_ImprimirTabelaB(yEuler_M, yExata, yTemp, a, b, n, sTemp);
            
            yRK2 = NRK2(f, a, b, n, y0);
            yTemp = abs(yExata - yRK2);
            sTemp = 'RK2';
            F_ImprimirTabelaB(yRK2, yExata, yTemp, a, b, n, sTemp);
            
            yRK4 = NRK4(f, a, b, n, y0);
            yTemp = abs(yExata - yRK4);
            sTemp = 'RK4';
            F_ImprimirTabelaB(yRK4, yExata, yTemp, a, b, n, sTemp);
            
            yAdams = NAdams(f, a, b, n, y0);
            yTemp = abs(yExata - yAdams);
            sTemp = 'Adams';
            F_ImprimirTabelaB(yAdams, yExata, yTemp, a, b, n, sTemp);
            
            [x, yODE45] = ode45(f, t, y0);
            yODE45 = yODE45.';
            yTemp = abs(yExata - yODE45);
            sTemp = 'ODE45';
            F_ImprimirTabelaB(yODE45, yExata, yTemp, a, b, n, sTemp);
            
            F_DesenharGraficoTodos(yEuler, yEuler_M, yRK2, yRK4, yAdams, yODE45, yExata, t);
    end

    if (~isempty(y) && opcao ~= 8)
        yErro = abs(yExata - y);
                
        F_ImprimirTabela(y, yExata, yErro, a, b, n, s);
        F_DesenharGrafico(y, yExata, t, s);
    end

    if opcao ~= 9
       tecla = input('\nPrima uma tecla para continuar...');
    end
end
        
        