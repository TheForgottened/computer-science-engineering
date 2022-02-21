% Fun��o que desenha um gr�fico se o utilizador pretender.
% Esta come�a por perguntar se o utilizador deseja visualizar o gr�fico.
%
% INPUT:
%   yExata - matriz com os valores exatos
%   yAprox - matriz com os valores aproximados
%   s - string com o nome do m�todo de aproxima��o usado
%
% Autor: TheForgotten | https://github.com/TheForgottened
% Data: 07/04/2020

function F_DesenharGrafico(yAprox, yExata, t, s)
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
        plot(t, yAprox, 'Color', [1 0 0]);
        hold on;
        plot(t, yExata, 'Color', [0 0 1]);
        plot(t, yAprox, 'o', 'MarkerSize', 10);
        plot(t, yExata, 'o', 'MarkerSize', 10);
        hold off;
        grid on;
        legend(s, 'Exata');
        shg;
    end
end