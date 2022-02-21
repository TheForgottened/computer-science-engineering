% MAQUINADERIVAPRIMITIVA Máquina para calcular derivadas e primitivas
% analiticamente (solução extata) e numericamente (solução aproximada)
% --- 12/01/2016   Arménio Correia   armenioc@isec.pt

function varargout = MaquinaDerivadaPrimitiva(varargin)
% MAQUINADERIVADAPRIMITIVA M-file for MaquinaDerivadaPrimitiva.fig
%      MAQUINADERIVADAPRIMITIVA, by itself, creates a new MAQUINADERIVADAPRIMITIVA or raises the existing
%      singleton*.
%
%      H = MAQUINADERIVADAPRIMITIVA returns the handle to a new MAQUINADERIVADAPRIMITIVA or the handle to
%      the existing singleton*.
%
%      MAQUINADERIVADAPRIMITIVA('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in MAQUINADERIVADAPRIMITIVA.M with the given input arguments.
%
%      MAQUINADERIVADAPRIMITIVA('Property','Value',...) creates a new MAQUINADERIVADAPRIMITIVA or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before MaquinaDerivadaPrimitiva_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to MaquinaDerivadaPrimitiva_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help MaquinaDerivadaPrimitiva

% Last Modified by GUIDE v2.5 02-May-2016 12:03:54

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @MaquinaDerivadaPrimitiva_OpeningFcn, ...
                   'gui_OutputFcn',  @MaquinaDerivadaPrimitiva_OutputFcn, ...
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


% --- Executes just before MaquinaDerivadaPrimitiva is made visible.
function MaquinaDerivadaPrimitiva_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to MaquinaDerivadaPrimitiva (see VARARGIN)

% Choose default command line output for MaquinaDerivadaPrimitiva
handles.output = hObject;

% Update handles structure
guidata(hObject, handles);

% Preparar o axesSolução para mostrar o resultado
set(hObject,'Name','Derivadas e Primitivas')
set(handles.axesSExata,'box','on');
set(handles.axesSExata,'xtick',[]);
set(handles.axesSExata,'ytick',[]);
     
% Configuração do Texto e atribuição a UserData
hSolucao = struct('hTexto',text('Parent', handles.axesSExata,...
                                     'interpreter','latex',...
                                     'fontsize',20,...
                                     'units','norm',...
                                     'pos',[.05 .5]));   
hSolucao.strF='sin(x)';
hSolucao.filhas(1)=DerivacaoNumerica('Visible','Off');  

set(hObject,'UserData',hSolucao); 
set(hSolucao.filhas(1),'UserData',handles);
pbAnaliticamente_Callback([],[], handles);


% UIWAIT makes MaquinaDerivadaPrimitiva wait for user response (see UIRESUME)
% uiwait(handles.figurePrincipal);


% --- Outputs from this function are returned to the command line.
function varargout = MaquinaDerivadaPrimitiva_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;


% --- Executes on button press in pbAnaliticamente.
function pbAnaliticamente_Callback(hObject, eventdata, handles)
% hObject    handle to pbAnaliticamente (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
getStrFuncao(handles);
hSolucao = get(handles.figurePrincipal,'UserData');
y=hSolucao.strF;
funcao=@(x) eval(vectorize(y));
 
Escolha02=get(handles.bgDerivadaPrimitiva,'SelectedObject');
EscolhaDP=find([handles.rbDerivada,handles.rbPrimitiva]==Escolha02);
 
syms x;
try  
    if (~isempty(y))
        switch EscolhaDP
            case 1
                dF=diff(funcao(x));
                set(hSolucao.hTexto,'String',['$' latex(dF) '$']);
            case 2
                pF=int(funcao(x));
                set(hSolucao.hTexto,'String',['$' latex(pF) '+ c' '$']);
        end
    end
catch Me
    errordlg(Me.message,'Erro','modal')
end



% --- Executes on button press in pbDNumerica.
function pbDNumerica_Callback(hObject, eventdata, handles)
% hObject    handle to pbDNumerica (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

getStrFuncao(handles);
hSolucao = get(handles.figurePrincipal, 'UserData');

y = hSolucao.strF;
htFuncao = findobj(hSolucao.filhas(1), 'Tag', 'tFuncao');

set(htFuncao, 'String', y);
set(hSolucao.filhas(1), 'Visible', 'on');

% --- Executes on button press in pbINumerica.
function pbINumerica_Callback(hObject, eventdata, handles)
% hObject    handle to pbINumerica (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)


% --- Executes on selection change in popupmenuF.
function popupmenuF_Callback(hObject, eventdata, handles)
% hObject    handle to popupmenuF (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: contents = cellstr(get(hObject,'String')) returns popupmenuF contents as cell array
%        contents{get(hObject,'Value')} returns selected item from popupmenuF


% --- Executes during object creation, after setting all properties.
function popupmenuF_CreateFcn(hObject, eventdata, handles)
% hObject    handle to popupmenuF (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: popupmenu controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function eG_Callback(hObject, eventdata, handles)
% hObject    handle to eG (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of eG as text
%        str2double(get(hObject,'String')) returns contents of eG as a double


% --- Executes during object creation, after setting all properties.
function eG_CreateFcn(hObject, eventdata, handles)
% hObject    handle to eG (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


% --- Executes when selected object is changed in bgFuncoes.
function bgFuncoes_SelectionChangeFcn(hObject, eventdata, handles)
% hObject    handle to the selected object in bgFuncoes 
% eventdata  structure with the following fields (see UIBUTTONGROUP)
%	EventName: string 'SelectionChanged' (read only)
%	OldValue: handle of the previously selected object or empty if none was selected
%	NewValue: handle of the currently selected object
% handles    structure with handles and user data (see GUIDATA)
getStrFuncao(handles);


% --- Executes when user attempts to close figurePrincipal.
function figurePrincipal_CloseRequestFcn(hObject, eventdata, handles)
% hObject    handle to figurePrincipal (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: delete(hObject) closes the figure
hSolucao=get(hObject,'UserData');
delete(hSolucao.filhas);
delete(hObject);

% --- Aceder a string/função selecionada ou introduzida.
function getStrFuncao(handles)
% handles    structure with handles and user data (see GUIDATA)
Escolha01=get(handles.bgFuncoes,'SelectedObject');
Escolha=find([handles.rbF,handles.rbG]==Escolha01);
switch Escolha
    case 1
        set(handles.eG,'Enable','off');
        set(handles.popupmenuF,'Enable','on'); 
        escolhaF=get(handles.popupmenuF,'Value');
        switch escolhaF
            case 1
                y='sin(x)';
            case 2
                y='cos(x)';                
            case 3
                y='exp(x)';
            case 4
                y='x^2';
            case 5
                y='log(x)';    
        end
     case 2
        set(handles.eG,'Enable','on');
        set(handles.popupmenuF,'Enable','off'); 
        y=get(handles.eG,'String');
end
hSolucao=get(handles.figurePrincipal,'UserData');
hSolucao.strF=y;
set(handles.figurePrincipal,'UserData',hSolucao);
       






