% Interface Geral para os PVI (gr�fica)
% Aplica��o dos variados m�todos num�ricos em MatLab
% Autor: TheForgotten | https://github.com/TheForgottened
% Data: 23/04/2020

function varargout = InterfaceGrafica(varargin)
% INTERFACEGRAFICA MATLAB code for InterfaceGrafica.fig
%      INTERFACEGRAFICA, by itself, creates a new INTERFACEGRAFICA or raises the existing
%      singleton*.
%
%      H = INTERFACEGRAFICA returns the handle to a new INTERFACEGRAFICA or the handle to
%      the existing singleton*.
%
%      INTERFACEGRAFICA('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in INTERFACEGRAFICA.M with the given input arguments.
%
%      INTERFACEGRAFICA('Property','Value',...) creates a new INTERFACEGRAFICA or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before InterfaceGrafica_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to InterfaceGrafica_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help InterfaceGrafica

% Last Modified by GUIDE v2.5 17-May-2020 18:47:08

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @InterfaceGrafica_OpeningFcn, ...
                   'gui_OutputFcn',  @InterfaceGrafica_OutputFcn, ...
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

clc;
clear;

% End initialization code - DO NOT EDIT
end


% --- Executes just before InterfaceGrafica is made visible.
function InterfaceGrafica_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to InterfaceGrafica (see VARARGIN)

% Choose default command line output for InterfaceGrafica
handles.output = hObject;

% Update handles structure
guidata(hObject, handles);

% UIWAIT makes InterfaceGrafica wait for user response (see UIRESUME)
% uiwait(handles.GUI);

MyAtualizar(handles);
end


% --- Outputs from this function are returned to the command line.
function varargout = InterfaceGrafica_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;
end


% --- Executes when user attempts to close GUI.
function GUI_CloseRequestFcn(hObject, eventdata, handles)
% hObject    handle to GUI (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: delete(hObject) closes the figure
S = questdlg('Deseja sair?','SAIR','Sim','N�o','Sim');

if strcmp(S,'N�o')
    return;
end

delete(hObject);
end



function tbFunF_Callback(hObject, eventdata, handles)
% hObject    handle to tbFunF (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of tbFunF as text
%        str2double(get(hObject,'String')) returns contents of tbFunF as a double
end


% --- Executes during object creation, after setting all properties.
function tbFunF_CreateFcn(hObject, eventdata, handles)
% hObject    handle to tbFunF (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end



function tbA_Callback(hObject, eventdata, handles)
% hObject    handle to tbA (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of tbA as text
%        str2double(get(hObject,'String')) returns contents of tbA as a double
end


% --- Executes during object creation, after setting all properties.
function tbA_CreateFcn(hObject, eventdata, handles)
% hObject    handle to tbA (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end



function tbB_Callback(hObject, eventdata, handles)
% hObject    handle to tbB (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of tbB as text
%        str2double(get(hObject,'String')) returns contents of tbB as a double
end


% --- Executes during object creation, after setting all properties.
function tbB_CreateFcn(hObject, eventdata, handles)
% hObject    handle to tbB (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end



function tbN_Callback(hObject, eventdata, handles)
% hObject    handle to tbN (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of tbN as text
%        str2double(get(hObject,'String')) returns contents of tbN as a double
end


% --- Executes during object creation, after setting all properties.
function tbN_CreateFcn(hObject, eventdata, handles)
% hObject    handle to tbN (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end



function tbU0_Callback(hObject, eventdata, handles)
% hObject    handle to tbU0 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of tbU0 as text
%        str2double(get(hObject,'String')) returns contents of tbU0 as a double
end


% --- Executes during object creation, after setting all properties.
function tbU0_CreateFcn(hObject, eventdata, handles)
% hObject    handle to tbU0 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end


% --- Executes on button press in pbAtualizar.
function pbAtualizar_Callback(hObject, eventdata, handles)
% hObject    handle to pbAtualizar (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
MyAtualizar(handles);
end

% --- Executes on button press in rbRK4.
function rbRK4_Callback(hObject, eventdata, handles)
% hObject    handle to rbRK4 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of rbRK4
end

% --- Executes on button press in rbTodos.
function rbTodos_Callback(hObject, eventdata, handles)
% hObject    handle to rbTodos (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of rbTodos
end

% --- Executes on button press in rbRK2.
function rbRK2_Callback(hObject, eventdata, handles)
% hObject    handle to rbRK2 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of rbRK2
end

% --- Executes on button press in rbEulerM.
function rbEulerM_Callback(hObject, eventdata, handles)
% hObject    handle to rbEulerM (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of rbEulerM
end

% --- Executes on button press in rbEuler.
function rbEuler_Callback(hObject, eventdata, handles)
% hObject    handle to rbEuler (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of rbEuler
end

function MyAtualizar(handles)
strF = get(handles.tbFunF, 'String');
strG = get(handles.tbFunG, 'String');
f = @(t,u,v) eval(vectorize(strF));
g = @(t,u,v) eval(vectorize(strG));

a = str2num(get(handles.tbA, 'String'));
b = str2num(get(handles.tbB, 'String'));
n = str2num(get(handles.tbN, 'String'));
v0 = str2num(get(handles.tbV0, 'String'));
u0 = str2num(get(handles.tbU0, 'String'));

escolhabg = get(handles.bgMetodos, 'SelectedObject');
escolhaM = find([handles.rbEuler, ...
              handles.rbEulerM, ...
              handles.rbRK2, ...
              handles.rbRK4, ...
              handles.rbTodos] == escolhabg);
escolhaA = get(handles.popEscolhas, 'Value');

if (escolhaA ~= 1)
    set(handles.tbFunF, 'Enable', 'off');
else
    set(handles.tbFunF, 'Enable', 'on');
end
          
testeFunc = MException('MATLAB:MyAtualizar:badFunc',....
                        'Introduza uma fun��o em t, u e v!');
                    
testeNReal = MException('MATLAB:MyAtualizar:badNReal',....
                        'Introduza um n�mero real!');
                    
testeNNatural = MException('MATLAB:MyAtualizar:badNNatural',....
                        'Introduza um n�mero natural!');
                    
testeB = MException('MATLAB:MyAtualizar:badB',....
                        'Introduza um n�mero maior que a!');
          
try  
    set(handles.tbFunF, 'BackgroundColor', 'white');
    set(handles.tbFunG, 'BackgroundColor', 'white');
    try
        fTeste=f(sym('u'), sym('v'), sym('t'));  
    catch
        set(handles.tbFunF, 'BackgroundColor', 'red');
        throw(testeFunc);
    end
    
    try
        fTeste=g(sym('u'), sym('v'), sym('t'));  
    catch
        set(handles.tbFunG, 'BackgroundColor', 'red');
        throw(testeFunc);
    end

    if ~(isscalar(a) && isreal(a))
        set(handles.tbA, 'BackgroundColor', 'red');
        throw(testeNReal);
    else
        set(handles.tbA, 'BackgroundColor', 'white');
    end
    
    if (isscalar(b) && isreal(b))
        if ~(b > a)
            set(handles.tbB, 'BackgroundColor', 'red');
            throw(testeB);
        end
        set(handles.tbB, 'BackgroundColor', 'white');
    else
        set(handles.tbB, 'BackgroundColor', 'red');
        throw(testeNReal);
    end
    
    if ~(isscalar(n) && fix(n) == n && n > 0)
        set(handles.tbN, 'BackgroundColor', 'red');
        throw(testeNNatural);
    else
        set(handles.tbN, 'BackgroundColor', 'white');
    end
    
    if ~(isscalar(u0) && isreal(u0))
        set(handles.tbU0, 'BackgroundColor', 'red');
        throw(testeNReal);
    else
        set(handles.tbU0, 'BackgroundColor', 'white');
    end
    
    if ~(isscalar(v0) && isreal(v0))
        set(handles.tbV0, 'BackgroundColor', 'red');
        throw(testeNReal);
    else
        set(handles.tbV0, 'BackgroundColor', 'white');
    end 
    
    switch escolhaA % problema de aplica��o
        case 1 % P�ndulo N�o Linear
            pendulo(handles, escolhaM, f, g, a, b, n, u0, v0);
        case 2 % SM Mola-Massa c/ Amortecimento
            mola(handles, escolhaM, f, g, a, b, n, u0, v0, strF, strG);
        case 3 % SM Mola-Massa s/ Amortecimento
            mola(handles, escolhaM, f, g, a, b, n, u0, v0, strF, strG);
        case 4 % Circuitos El�tricos Modelados por ED de ordem 2
            circEle(handles, escolhaM, f, g, a, b, n, u0, v0, strF, strG);
        case 5 % Modelo Vibrat�rio Mec�nico
            mola(handles, escolhaM, f, g, a, b, n, u0, v0, strF, strG);
    end
    
    hold off;
    
    if get(handles.cbGrelha, 'Value')
        grid on;
    end

catch Me
    errordlg(Me.message,'ERRO','modal');
end

end



function tbFunG_Callback(hObject, eventdata, handles)
% hObject    handle to tbFunG (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of tbFunG as text
%        str2double(get(hObject,'String')) returns contents of tbFunG as a double
end


% --- Executes during object creation, after setting all properties.
function tbFunG_CreateFcn(hObject, eventdata, handles)
% hObject    handle to tbFunG (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end


% --- Executes on selection change in popEscolhas.
function popEscolhas_Callback(hObject, eventdata, handles)
% hObject    handle to popEscolhas (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: contents = cellstr(get(hObject,'String')) returns popEscolhas contents as cell array
%        contents{get(hObject,'Value')} returns selected item from popEscolhas
end


% --- Executes during object creation, after setting all properties.
function popEscolhas_CreateFcn(hObject, eventdata, handles)
% hObject    handle to popEscolhas (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: popupmenu controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end


% --------------------------------------------------------------------
function mAutor_Callback(hObject, eventdata, handles)
% hObject    handle to mAutor (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
Autor();
end


function tbV0_Callback(hObject, eventdata, handles)
% hObject    handle to tbV0 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of tbV0 as text
%        str2double(get(hObject,'String')) returns contents of tbV0 as a double
end


% --- Executes during object creation, after setting all properties.
function tbV0_CreateFcn(hObject, eventdata, handles)
% hObject    handle to tbV0 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end


% --- Executes on button press in cbGrelha.
function cbGrelha_Callback(hObject, eventdata, handles)
% hObject    handle to cbGrelha (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of cbGrelha
end

function pendulo(handles, escolha, f, g, a, b, n, u0, v0)
EULER = 1;
EULERM = 2;
RK2 = 3;
RK4 = 4;
TODOS = 5;

switch escolha
        case EULER
            [t, uEuler, vEuler] = NEulerSED(f, g, a, b, n, u0, v0);
            plot(t, uEuler, 'r');
            hold on;
            plot(t, vEuler, 'b');
            legend('Deslocamento Aprox.', 'Velocidade Aprox.');
            tabela = [t.', uEuler.',  vEuler.']; 
            set(handles.uiTabela, 'Data', num2cell(tabela)); 
            set(handles.uiTabela, 'ColumnName',...
                    [{'t'}, {'Euler_U'}, {'Euler_V'}]);
                
        case EULERM
            [t, uEulerM, vEulerM] = NEuler_MSED(f, g, a, b, n, u0, v0);
            plot(t, uEulerM, 'r');
            hold on;
            plot(t, vEulerM, 'b');
            legend('Deslocamento Aprox.', 'Velocidade Aprox.');
            tabela = [t.', uEulerM.',  vEulerM.']; 
            set(handles.uiTabela, 'Data', num2cell(tabela)); 
            set(handles.uiTabela, 'ColumnName',...
                    [{'t'}, {'EulerM_U'}, {'EulerM_V'}]);
                
        case RK2
            [t, uRK2, vRK2] = NRK2SED(f, g, a, b, n, u0, v0);
            plot(t, uRK2, 'r');
            hold on;
            plot(t, vRK2, 'b');
            legend('Deslocamento Aprox.', 'Velocidade Aprox.');
            tabela = [t.', uRK2.',  vRK2.']; 
            set(handles.uiTabela, 'Data', num2cell(tabela)); 
            set(handles.uiTabela, 'ColumnName',...
                    [{'t'}, {'RK2_U'}, {'RK2_V'}]);
                
        case RK4
            [t, uRK4, vRK4] = NRK4SED(f, g, a, b, n, u0, v0);
            plot(t, uRK4, 'r');
            hold on;
            plot(t, vRK4, 'b');
            legend('Deslocamento Aprox.', 'Velocidade Aprox.');
            tabela = [t.', uRK4.',  vRK4.']; 
            set(handles.uiTabela, 'Data', num2cell(tabela)); 
            set(handles.uiTabela, 'ColumnName',...
                    [{'t'}, {'RK4_U'}, {'RK4_V'}]);
                
        case TODOS
            [t, uEuler, vEuler] = NEulerSED(f, g, a, b, n, u0, v0);
            [~, uEulerM, vEulerM] = NEuler_MSED(f, g, a, b, n, u0, v0);
            [~, uRK2, vRK2] = NRK2SED(f, g, a, b, n, u0, v0);
            [~, uRK4, vRK4] = NRK4SED(f, g, a, b, n, u0, v0);
            
            plot(t, uEuler, 'Color', [1 0 0]);
            hold on;
            plot(t, vEuler, 'Color', [0 0 1]);
            
            plot(t, uEulerM, 'Color', [0.75 0 0]);
            plot(t, vEulerM, 'Color', [0 0 0.75]);
            
            plot(t, uRK2, 'Color', [0.5 0 0]);
            plot(t, vRK2, 'Color', [0 0 0.5]);
            
            plot(t, uRK4, 'Color', [0.25 0 0]);
            plot(t, vRK4, 'Color', [0 0 0.25]);
            
            tabela = [t.', uEuler.', vEuler.', uEulerM.', vEulerM.', ... 
                    uRK2.', vRK2.', uRK4.', vRK4.'];
                    
            set(handles.uiTabela, 'Data', num2cell(tabela)); 
            
            set(handles.uiTabela, 'ColumnName',...
                    [{'t'}, {'Euler_U'}, {'Euler_V'}, {'EulerM_U'}, ...
                    {'EulerM_V'}, {'RK2_U'}, {'RK2_V'},  {'RK4_U'}, ... 
                    {'RK4_V'}]);
                
            legend('Deslocamento Euler', 'Velocidade Euler', ...
                    'Deslocamento EulerM', 'Velocidade EulerM', ...
                    'Deslocamento RK2', 'Velocidade RK2', ...
                    'Deslocamento RK4', 'Velocidade RK4');
end
end

function mola(handles, escolha, f, g, a, b, n, u0, v0, strF, strG)
EULER = 1;
EULERM = 2;
RK2 = 3;
RK4 = 4;
TODOS = 5;

[u, ~] = NExataSED(strF, strG, a, u0, v0);

switch escolha
        case EULER
            [t, uEuler, ~] = NEulerSED(f, g, a, b, n, u0, v0);
            plot(t, uEuler, 'r');
            hold on;
            
            uExata = u(t);
            
            uExata = double(uExata);
            
            uEulerErro = abs(uEuler - uExata);
            
            plot(t, uExata, 'g');
            legend('Deslocamento Aprox.', 'Deslocamento Ex.');
            tabela = [t.', uEuler.', uEulerErro.', uExata.']; 
            set(handles.uiTabela, 'Data', num2cell(tabela)); 
            set(handles.uiTabela, 'ColumnName',...
                    [{'t'}, {'Euler'}, {'Erro'}, {'Exata'}]);
                
        case EULERM
            [t, uEulerM, ~] = NEuler_MSED(f, g, a, b, n, u0, v0);
            plot(t, uEulerM, 'r');
            hold on;
            
            uExata = u(t);
            
            uExata = double(uExata);
            
            uEulerMErro = abs(uEulerM - uExata);
            
            plot(t, uExata, 'g');
            legend('Deslocamento Aprox.', 'Deslocamento Ex.');
            tabela = [t.', uEulerM.', uEulerMErro.', uExata.'];
            set(handles.uiTabela, 'Data', num2cell(tabela)); 
            set(handles.uiTabela, 'ColumnName',...
                    [{'t'}, {'EulerM'}, {'Erro'}, {'Exata'}]);
                
        case RK2
            [t, uRK2, ~] = NRK2SED(f, g, a, b, n, u0, v0);
            plot(t, uRK2, 'r');
            hold on;
            
            uExata = u(t);
            
            uExata = double(uExata);
            
            uRK2Erro = abs(uRK2 - uExata);
            
            plot(t, uExata, 'g');
            legend('Deslocamento Aprox.', 'Deslocamento Ex.');
            tabela = [t.', uRK2.', uRK2Erro.', uExata.'];
            set(handles.uiTabela, 'Data', num2cell(tabela)); 
            set(handles.uiTabela, 'ColumnName',...
                    [{'t'}, {'RK2'}, {'Erro'}, {'Exata'}]);
                
        case RK4
            [t, uRK4, ~] = NRK4SED(f, g, a, b, n, u0, v0);
            plot(t, uRK4, 'r');
            hold on;
            
            uExata = u(t);
            
            uExata = double(uExata);
            
            uRK4Erro = abs(uRK4 - uExata);
            
            plot(t, uExata, 'g');
            legend('Deslocamento Aprox.', 'Deslocamento Ex.');
            tabela = [t.', uRK4.', uRK4Erro.', uExata.']; 
            set(handles.uiTabela, 'Data', num2cell(tabela)); 
            set(handles.uiTabela, 'ColumnName',...
                    [{'t'}, {'RK4'}, {'Erro'}, {'Exata'}]);
                
        case TODOS
            [t, uEuler, ~] = NEulerSED(f, g, a, b, n, u0, v0);
            [~, uEulerM, ~] = NEuler_MSED(f, g, a, b, n, u0, v0);
            [~, uRK2, ~] = NRK2SED(f, g, a, b, n, u0, v0);
            [~, uRK4, ~] = NRK4SED(f, g, a, b, n, u0, v0);
            
            plot(t, uEuler, 'Color', [1 0 0]);
            hold on;
            plot(t, uEulerM, 'Color', [0.75 0 0]);
            plot(t, uRK2, 'Color', [0.5 0 0]);   
            plot(t, uRK4, 'Color', [0.25 0 0]);
            
            uExata = u(t);
            
            uExata = double(uExata);
            
            uEulerErro = abs(uEuler - uExata);     
            uEulerMErro = abs(uEulerM - uExata);
            uRK2Erro = abs(uRK2 - uExata);   
            uRK4Erro = abs(uRK4 - uExata);
            
            plot(t, uExata, 'g');         
            tabela = [t.', uEuler.', uEulerErro.', uEulerM.', uEulerMErro.' ...
                    uRK2.', uRK2Erro.', uRK4.', uRK4Erro.', uExata.'];
                    
            set(handles.uiTabela, 'Data', num2cell(tabela)); 
            
            set(handles.uiTabela, 'ColumnName',...
                    [{'t'}, {'Euler'}, {'Euler_Erro'}, ...
                    {'EulerM'}, {'EulerM_Erro'}, {'RK2'}, {'RK2_Erro'}, ...
                    {'RK4'}, {'RK4_Erro'}, {'Exata'}]);
                
            legend('Deslocamento Euler', 'Deslocamento EulerM', ...
                    'Deslocamento RK2', 'Deslocamento RK4', ...
                    'Deslocamento Ex.');
end
end

function circEle(handles, escolha, f, g, a, b, n, u0, v0, strF, strG)
EULER = 1;
EULERM = 2;
RK2 = 3;
RK4 = 4;
TODOS = 5;

[u, ~] = NExataSED(strF, strG, a, u0, v0);

switch escolha
        case EULER
            [t, uEuler, ~] = NEulerSED(f, g, a, b, n, u0, v0);
            plot(t, uEuler, 'r');
            hold on;
            
            uExata = u(t);
            
            uExata = double(uExata);
            
            uEulerErro = abs(uEuler - uExata);
            
            plot(t, uExata, 'g');
            legend('Corrente Aprox.', 'Corrente Ex.');
            tabela = [t.', uEuler.', uEulerErro.', uExata.']; 
            set(handles.uiTabela, 'Data', num2cell(tabela)); 
            set(handles.uiTabela, 'ColumnName',...
                    [{'t'}, {'Euler'}, {'Erro'}, {'Exata'}]);
                
        case EULERM
            [t, uEulerM, ~] = NEuler_MSED(f, g, a, b, n, u0, v0);
            plot(t, uEulerM, 'r');
            hold on;
            
            uExata = u(t);
            
            uExata = double(uExata);
            
            uEulerMErro = abs(uEulerM - uExata);
            
            plot(t, uExata, 'g');
            legend('Corrente Aprox.', 'Corrente Ex.');
            tabela = [t.', uEulerM.', uEulerMErro.', uExata.'];
            set(handles.uiTabela, 'Data', num2cell(tabela)); 
            set(handles.uiTabela, 'ColumnName',...
                    [{'t'}, {'EulerM'}, {'Erro'}, {'Exata'}]);
                
        case RK2
            [t, uRK2, ~] = NRK2SED(f, g, a, b, n, u0, v0);
            plot(t, uRK2, 'r');
            hold on;
            
            uExata = u(t);
            
            uExata = double(uExata);
            
            uRK2Erro = abs(uRK2 - uExata);
            
            plot(t, uExata, 'g');
            legend('Corrente Aprox.', 'Corrente Ex.');
            tabela = [t.', uRK2.', uRK2Erro.', uExata.'];
            set(handles.uiTabela, 'Data', num2cell(tabela)); 
            set(handles.uiTabela, 'ColumnName',...
                    [{'t'}, {'RK2'}, {'Erro'}, {'Exata'}]);
                
        case RK4
            [t, uRK4, ~] = NRK4SED(f, g, a, b, n, u0, v0);
            plot(t, uRK4, 'r');
            hold on;
            
            uExata = u(t);
            
            uExata = double(uExata);
            
            uRK4Erro = abs(uRK4 - uExata);
            
            plot(t, uExata, 'g');
            legend('Corrente Aprox.', 'Corrente Ex.');
            tabela = [t.', uRK4.', uRK4Erro.', uExata.']; 
            set(handles.uiTabela, 'Data', num2cell(tabela)); 
            set(handles.uiTabela, 'ColumnName',...
                    [{'t'}, {'RK4'}, {'Erro'}, {'Exata'}]);
                
        case TODOS
            [t, uEuler, ~] = NEulerSED(f, g, a, b, n, u0, v0);
            [~, uEulerM, ~] = NEuler_MSED(f, g, a, b, n, u0, v0);
            [~, uRK2, ~] = NRK2SED(f, g, a, b, n, u0, v0);
            [~, uRK4, ~] = NRK4SED(f, g, a, b, n, u0, v0);
            
            plot(t, uEuler, 'Color', [1 0 0]);
            hold on;
            plot(t, uEulerM, 'Color', [0.75 0 0]);
            plot(t, uRK2, 'Color', [0.5 0 0]);   
            plot(t, uRK4, 'Color', [0.25 0 0]);
            
            uExata = u(t);
            
            uExata = double(uExata);
            
            uEulerErro = abs(uEuler - uExata);     
            uEulerMErro = abs(uEulerM - uExata);
            uRK2Erro = abs(uRK2 - uExata);   
            uRK4Erro = abs(uRK4 - uExata);
            
            plot(t, uExata, 'g');         
            tabela = [t.', uEuler.', uEulerErro.', uEulerM.', uEulerMErro.' ...
                    uRK2.', uRK2Erro.', uRK4.', uRK4Erro.', uExata.'];
                    
            set(handles.uiTabela, 'Data', num2cell(tabela)); 
            
            set(handles.uiTabela, 'ColumnName',...
                    [{'t'}, {'Euler'}, {'Euler_Erro'}, ...
                    {'EulerM'}, {'EulerM_Erro'}, {'RK2'}, {'RK2_Erro'}, ...
                    {'RK4'}, {'RK4_Erro'}, {'Exata'}]);
                
            legend('Corrente Euler', 'Corrente EulerM', ...
                    'Corrente RK2', 'Corrente RK4', ...
                    'Corrente Ex.');
end
end


% --------------------------------------------------------------------
function mExcel_Callback(hObject, eventdata, handles)
% hObject    handle to mExcel (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

filename = inputdlg('Nome do ficheiro:', 'Exportar para Excel', [1 30], {'valores'});
filename = string(filename) + '.xlsx';

l = get(handles.uiTabela, 'ColumnName');
l = l.';
tabela = get(handles.uiTabela, 'Data');

writecell(tabela, filename, 'Sheet', 1, 'Range', 'A2');
writecell(l, filename, 'Sheet', 1, 'Range', 'A1');
end
