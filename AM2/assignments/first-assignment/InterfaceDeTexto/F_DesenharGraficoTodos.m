% Fun��o que desenha o gr�fico se o utilizador pretender.
% Esta fun��o � extremamente espec�fica ao problema colocado.
% Esta come�a por perguntar se o utilizador deseja visualizar o gr�fico.
%
% INPUT:
%   yExata - matriz com os valores exatos
%   yAprox - matriz com os valores aproximados
%   s - string com o nome do m�todo de aproxima��o usado
%
% Autor: TheForgotten | https://github.com/TheForgottened
% Data: 07/04/2020

function F_DesenharGraficoTodos(yEuler, yEuler_M, yRK2, yRK4, yAdams, yODE45, yExata, t)
    fprintf('\nDeseja visualizar o gr�fico?\nInsira 1 para SIM e 0 para N�O.\n');
    
    while 1
        opcao = input('Op��o: ', 's');

        [temp1, temp2] = str2num(opcao);

        if(temp2 && isscalar(temp1) && temp1 == floor(temp1) && (temp1 == 0 || temp1 == 1))
            opcao = temp1;
            break;
        else
            fprintf(2, 'N�mero inv�lido!\n\n');
        end
    end
    
    if ~opcao
        return;
    else
        plot(t, yEuler, 'Color', [0 1 0]);
        hold on;
        plot(t, yEuler_M, 'Color', [1 0 0]);
        plot(t, yRK2, 'Color', [1 1 0]);
        plot(t, yRK4, 'Color', [1 0 1]);
        plot(t, yAdams, 'Color', [0 1 1]);
        plot(t, yODE45, 'Color', [0.6980 0.1333 0.1333]);
        plot(t, yExata, 'Color', [0 0 1]);
        hold off;
        grid on;
        legend('Euler', 'Euler_M', 'RK2', 'RK4', 'Adams', 'ODE45', 'Exata');
        shg;
    end
end