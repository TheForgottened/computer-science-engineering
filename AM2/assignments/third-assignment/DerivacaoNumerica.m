% DerivacaoNumerica Derivação Numérica
% Máquina para calcular soluções aproximadas de derivadas
% --- 12/01/2016  Arménio Correia   armenioc@isec.pt
function varargout = DerivacaoNumerica(varargin)
% DERIVACAONUMERICA M-file for DerivacaoNumerica.fig
%      DERIVACAONUMERICA, by itself, creates a new DERIVACAONUMERICA or raises the existing
%      singleton*.
%
%      H = DERIVACAONUMERICA returns the handle to a new DERIVACAONUMERICA or the handle to
%      the existing singleton*.
%
%      DERIVACAONUMERICA('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in DERIVACAONUMERICA.M with the given input arguments.
%
%      DERIVACAONUMERICA('Property','Value',...) creates a new DERIVACAONUMERICA or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before DerivacaoNumerica_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to DerivacaoNumerica_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help DerivacaoNumerica

% Last Modified by GUIDE v2.5 26-May-2020 11:22:28

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @DerivacaoNumerica_OpeningFcn, ...
                   'gui_OutputFcn',  @DerivacaoNumerica_OutputFcn, ...
                   'gui_LayoutFcn',  [] , ...
                   'gui_Callback',   []);
if nargin && ischar(varargin{1})
    gui_State.gui_Callback = str2func(varargin{1});
end

if nargout
    [varargout{1:nargout}] = gui_mainfcn(gui_State, varargin{:});
else
    gui_mainfcn(gui_State, varargin{:});
end
% End initialization code - DO NOT EDIT
end

% --- Executes just before DerivacaoNumerica is made visible.
function DerivacaoNumerica_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to DerivacaoNumerica (see VARARGIN)
 
% Choose default command line output for DerivacaoNumerica
handles.output = hObject;
 
% Update handles structure
guidata(hObject, handles);
 
% UIWAIT makes DerivacaoNumerica wait for user response (see UIRESUME)
% uiwait(handles.figureDerivacaoNumerica);
MyAtualizar(handles);
end

% --- Outputs from this function are returned to the command line.
function varargout = DerivacaoNumerica_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;
end

% --- Executes on button press in pbAtualizar.
function pbAtualizar_Callback(hObject, eventdata, handles)
% hObject    handle to pbAtualizar (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
MyAtualizar(handles);
end

function eA_Callback(hObject, eventdata, handles)
% hObject    handle to eA (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of eA as text
%        str2double(get(hObject,'String')) returns contents of eA as a double
end

% --- Executes during object creation, after setting all properties.
function eA_CreateFcn(hObject, eventdata, handles)
% hObject    handle to eA (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end


function eH_Callback(hObject, eventdata, handles)
% hObject    handle to eH (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of eH as text
%        str2double(get(hObject,'String')) returns contents of eH as a double
end

% --- Executes during object creation, after setting all properties.
function eH_CreateFcn(hObject, eventdata, handles)
% hObject    handle to eH (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end


function eB_Callback(hObject, eventdata, handles)
% hObject    handle to eB (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of eB as text
%        str2double(get(hObject,'String')) returns contents of eB as a double
end

% --- Executes during object creation, after setting all properties.
function eB_CreateFcn(hObject, eventdata, handles)
% hObject    handle to eB (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end

% --- Executes when user attempts to close figureDerivacaoNumerica.
function figureDerivacaoNumerica_CloseRequestFcn(hObject, eventdata, handles)
% hObject    handle to figureDerivacaoNumerica (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
 
% Hint: delete(hObject) closes the figure
%delete(hObject);
set(hObject,'Visible','Off');
end

% --- Função auxiliar associada ao botão Atualizar.
function MyAtualizar(handles)
% handles estrutura de dados com as handles para os objetos...
syms x;
cla reset;

strF = get(handles.tbF,'String');
f=@(x) eval(vectorize(char(strF)));
df = diff(f(x));
df2 = diff(df);

a = str2num(get(handles.eA, 'String'));
b = str2num(get(handles.eB, 'String'));
h = str2num(get(handles.eH, 'String'));

escolhabg = get(handles.bgMetodos, 'SelectedObject');
escolhaM = find([handles.rbDFP2, ...
              handles.rbDFR2, ...
              handles.rbDFP3, ...
              handles.rbDFR3, ...
              handles.rbDFC3, ...
              handles.rbDF2D, ...
              handles.rbTodos] == escolhabg);
          
testeFun = MException('MATLAB:MyAtualizar:badFunc',....
                        'Introduza uma função em x!');
                    
testeNReal = MException('MATLAB:MyAtualizar:badNReal',....
                        'Introduza um número real!');                
                    
testeB = MException('MATLAB:MyAtualizar:badB',....
                        'Introduza um número maior que a!');
try
    set(handles.tbF, 'BackgroundColor', 'white');
    
    try
        fTest = f(sym('x'));
    catch
        set(handles.tbF, 'BackgroundColor', 'red');
        throw(testeFun);
    end
   
     if ~(isscalar(a) && isreal(a))
        set(handles.eA, 'BackgroundColor', 'red');
        throw(testeNReal);
    else
        set(handles.eA, 'BackgroundColor', 'white');
    end
    
    if (isscalar(b) && isreal(b))
        if ~(b > a)
            set(handles.eB, 'BackgroundColor', 'red');
            throw(testeB);
        end
        set(handles.eB, 'BackgroundColor', 'white');
    else
        set(handles.eB, 'BackgroundColor', 'red');
        throw(testeNReal);
    end
    
    if ~(isscalar(h) && isreal(h))
        set(handles.eH, 'BackgroundColor', 'red');
        throw(testeNReal);
    else
        set(handles.eH, 'BackgroundColor', 'white');
    end  
    
    switch escolhaM
        case 1 % DF em 2 pontos - progressivas
            [x, y, DFP2] = NDerivacaoDFP2(f, a, b, h);
            sExata = eval(df);
            
            if size(sExata) == 1
                sExata = zeros(length(x), 1) + sExata;
                sExata = sExata.';
            end
            
            erroDFP2 = abs(sExata - DFP2);
            
            plot(x, y, 'b');
            hold on;
            plot(x, DFP2, 'r');
            plot(x, sExata, 'k');
            hold off;
            legend('Função', 'D.F. em 2 P. - Progressivas', 'D. Exata');
            tabela = [x.', y.', sExata.', DFP2.', erroDFP2.'];
            
            set(handles.uitabela, 'Data', num2cell(tabela));
            set(handles.uitabela, 'ColumnName', ...
                    [{'x'}, {'y'}, {'D. Exata'}, ...
                    {'D. F. em 2 P. - Progressivas'}, {'Erro'}]);     	
        
        case 2 % DF em 2 pontos - regressivas
            [x, y, DFR2] = NDerivacaoDFR2(f, a, b, h);
            sExata = eval(df);
            
            if size(sExata) == 1
                sExata = zeros(length(x), 1) + sExata;
                sExata = sExata.';
            end
            
            erroDFR2 = abs(sExata - DFR2);
            
            plot(x, y, 'b');
            hold on;
            plot(x, DFR2, 'r');
            plot(x, sExata, 'k');
            hold off;
            legend('Função', 'D.F. em 2 P. - Regressivas', 'D. Exata');
            tabela = [x.', y.', sExata.', DFR2.', erroDFR2.'];
            
            set(handles.uitabela, 'Data', num2cell(tabela));
            set(handles.uitabela, 'ColumnName', ...
                    [{'x'}, {'y'}, {'D. Exata'}, ...
                    {'D. F. em 2 P. - Regressivas'}, {'Erro'}]);
                   
        case 3 % DF em 3 pontos - progressivas
            [x, y, DFP3] = NDerivacaoDFP3(f, a, b, h);
            sExata = eval(df);
            
            if size(sExata) == 1
                sExata = zeros(length(x), 1) + sExata;
                sExata = sExata.';
            end
            
            erroDFP3 = abs(sExata - DFP3);
            
            plot(x, y, 'b');
            hold on;
            plot(x, DFP3, 'r');
            plot(x, sExata, 'k');
            hold off;
            legend('Função', 'D.F. em 3 P. - Progressivas', 'D. Exata');
            tabela = [x.', y.', sExata.', DFP3.', erroDFP3.'];
            
            set(handles.uitabela, 'Data', num2cell(tabela));
            set(handles.uitabela, 'ColumnName', ...
                    [{'x'}, {'y'}, {'D. Exata'}, ...
                    {'D. F. em 3 P. - Progressivas'}, {'Erro'}]);

        case 4 % DF em 3 pontos - regressivas
            [x, y, DFR3] = NDerivacaoDFR3(f, a, b, h);
            sExata = eval(df);
            
            if size(sExata) == 1
                sExata = zeros(length(x), 1) + sExata;
                sExata = sExata.';
            end
            
            erroDFR3 = abs(sExata - DFR3);
            
            plot(x, y, 'b');
            hold on;
            plot(x, DFR3, 'r');
            plot(x, sExata, 'k');
            hold off;
            legend('Função', 'D.F. em 3 P. - Regressivas', 'D. Exata');
            tabela = [x.', y.', sExata.', DFR3.', erroDFR3.'];
            
            set(handles.uitabela, 'Data', num2cell(tabela));
            set(handles.uitabela, 'ColumnName', ...
                    [{'x'}, {'y'}, {'D. Exata'}, ...
                    {'D. F. em 3 P. - Regressivas'}, {'Erro'}]);
            
        case 5 % DF em 3 pontos - centradas
            [x, y, DFC3] = NDerivacaoDFC3(f, a, b, h);
            sExata = eval(df);
            
            if size(sExata) == 1
                sExata = zeros(length(x), 1) + sExata;
                sExata = sExata.';
            end
            
            erroDFC3 = abs(sExata - DFC3);
            
            plot(x, y, 'b');
            hold on;
            plot(x, DFC3, 'r');
            plot(x, sExata, 'k');
            hold off;
            legend('Função', 'D.F. em 3 P. - Centrais', 'D. Exata');
            tabela = [x.', y.', sExata.', DFC3.', erroDFC3.'];
            
            set(handles.uitabela, 'Data', num2cell(tabela));
            set(handles.uitabela, 'ColumnName', ...
                    [{'x'}, {'y'}, {'D. Exata'}, ...
                    {'D. F. em 3 P. - Centrais'}, {'Erro'}]);

        case 6 % DF em 3 pontos - 2nd derivada
            [x, y, DF2D] = NDerivacaoDF2D(f, a, b, h);
            sExata2 = eval(df2);
            
            if size(sExata2) == 1
                sExata2 = zeros(length(x), 1) + sExata2;
                sExata2 = sExata2.';
            end
            
            erroDF2D = abs(sExata2 - DF2D);
            
            plot(x, y, 'b');
            hold on;
            plot(x, DF2D, 'r');
            plot(x, sExata2, 'k');
            hold off;
            legend('Função', 'D.F. em 3 P. - 2a D.', '2a D. Exata');
            tabela = [x.', y.', sExata2.', DF2D.', erroDF2D.'];
            
            set(handles.uitabela, 'Data', num2cell(tabela));
            set(handles.uitabela, 'ColumnName', ...
                    [{'x'}, {'y'}, {'2a D. Exata'}, ...
                    {'D. F. em 3 P. - 2a D.'}, {'Erro'}]);
            
        case 7 % TODOS
            [x, y, DFP2] = NDerivacaoDFP2(f, a, b, h);
            [~, ~, DFR2] = NDerivacaoDFR2(f, a, b, h);
            [~, ~, DFP3] = NDerivacaoDFP3(f, a, b, h);
            [~, ~, DFR3] = NDerivacaoDFR3(f, a, b, h);
            [~, ~, DFC3] = NDerivacaoDFC3(f, a, b, h);
            [~, ~, DF2D] = NDerivacaoDF2D(f, a, b, h);
            sExata = eval(df);
            sExata2 = eval(df2);
            
            if size(sExata) == 1
                sExata = zeros(length(x), 1) + sExata;
                sExata = sExata.';
            end
            
            if size(sExata2) == 1
                sExata2 = zeros(length(x), 1) + sExata2;
                sExata2 = sExata2.';
            end
            
            erroDFP2 = abs(sExata - DFP2);
            erroDFR2 = abs(sExata - DFR2);
            erroDFP3 = abs(sExata - DFP3);
            erroDFR3 = abs(sExata - DFR3);
            erroDFC3 = abs(sExata - DFC3);
            erroDF2D = abs(sExata2 - DF2D);
            
            plot(x, y, 'b');
            hold on;
            
            plot(x, DFP2, 'Color', [0 1 0]);
            plot(x, DFR2, 'Color', [0 0.75 0]);
            plot(x, DFP3, 'Color', [1 0 1]);
            plot(x, DFR3, 'Color', [1 0 0.75]);
            plot(x, DFC3, 'Color', [0.75 0 1]);
            plot(x, DF2D, 'Color', [0.75 0 0.75]);
            plot(x, sExata, 'k');
            plot(x, sExata2, 'r');
            
            legend('Função', 'D.F. em 2 P. - Progressivas', ...
                    'D.F. em 2 P. - Regressivas', ...
                    'D.F. em 3 P. - Progressivas', ...
                    'D.F. em 3 P. - Regressivas', ...
                    'D.F. em 3 P. - Centrais', ...
                    'D.F. em 3 P. - 2a D.', ...
                    'D. Exata', '2a D. Exata')
                
            tabela = [x.', y.', sExata.', sExata2.', DFP2.', erroDFP2.', ...
                        DFR2.', erroDFR2.', DFP3.', erroDFP3.', ...
                        DFR3.', erroDFR3.', DFC3.', erroDFC3.', ...
                        DF2D.', erroDF2D.'];
            
            set(handles.uitabela, 'Data', num2cell(tabela));
            set(handles.uitabela, 'ColumnName', ...
                    [{'x'}, {'y'}, {'D. Exata'}, {'2a D. Exata'},...
                    {'D. F. em 2 P. - Progressivas'}, {'Erro Prog. 2P'}, ...
                    {'D. F. em 2 P. - Regressivas'}, {'Erro Regr. 2P'}, ...
                    {'D. F. em 3 P. - Progressivas'}, {'Erro Prog. 3P'}, ...
                    {'D. F. em 3 P. - Regressivas'}, {'Erro Regr. 3P'}, ...
                    {'D. F. em 3 P. - Centrais'}, {'Erro Centrais 3P'}, ...
                    {'D. F. em 3 P. - 2a D.'}, {'Erro 2a D 3P'}]);     
    end

    if get(handles.cbGrid, 'Value')
        grid on;
    else
        grid off;
    end
    
catch Me
    errordlg(Me.message,'ERRO','modal');
end
end

% erroDFP=abs(dydxExata-dydx);
% plot(x,y,'-bo');
% hold on
% plot(x,dydx,'-r+');
% plot(x,dydxExata,'-ks');
% hold off;
% legend('Função','DerivadaDFP','DExata');
% set(handles.uitabela,'ColumnName',[{'x'},{'y'},{'DNumerica'},{'DExata'},{'Erro'}]);
% set(handles.uitabela,'Data',num2cell([x.',y.',dydx.',dydxExata.',erroDFP.']));



function tbF_Callback(hObject, eventdata, handles)
% hObject    handle to tbF (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of tbF as text
%        str2double(get(hObject,'String')) returns contents of tbF as a double
end

% --- Executes during object creation, after setting all properties.
function tbF_CreateFcn(hObject, eventdata, handles)
% hObject    handle to tbF (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end


% --------------------------------------------------------------------
function mExcel_Callback(hObject, eventdata, handles)
% hObject    handle to mExcel (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

filename = inputdlg('Nome do ficheiro:', 'Exportar para Excel', [1 30], {'valores'});
filename = string(filename) + '.xlsx';

l = get(handles.uitabela, 'ColumnName');
l = l.';
tabela = get(handles.uitabela, 'Data');

writecell(tabela, filename, 'Sheet', 1, 'Range', 'A2');
writecell(l, filename, 'Sheet', 1, 'Range', 'A1');
end


% --- Executes on button press in cbGrid.
function cbGrid_Callback(hObject, eventdata, handles)
% hObject    handle to cbGrid (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of cbGrid
if get(handles.cbGrid, 'Value')
    grid on;
else
    grid off;
end
end

% --- Executes on button press in cbLegendAxes.
function cbLegendAxes_Callback(hObject, eventdata, handles)
% hObject    handle to cbLegendAxes (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of cbLegendAxes
if get(handles.cbLegendAxes, 'Value')
    xlabel('Eixo dos xx');
    ylabel('Eixo dos yy');
else
    xlabel('');
    ylabel('');
end
end
