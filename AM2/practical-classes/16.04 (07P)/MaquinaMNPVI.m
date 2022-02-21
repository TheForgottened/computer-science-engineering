%MaquinaMNPVI  Máquina Métodos Numéricos PVI.
%   Interface gráfica (GUI) - parte02 da atividade01 de AM2
%   CHAMADA DAS FUNÇÕES: NEuler, NRK2
%
%   23/03/2020  - ArménioCorreia .: armenioc@isec.pt
%   aula07/2020 - aulas práticas de AM2.EI da 7ªsemana de aulas

function varargout = MaquinaMNPVI(varargin)
% MAQUINAMNPVI M-file for MaquinaMNPVI.fig
%      MAQUINAMNPVI, by itself, creates a new MAQUINAMNPVI or raises the existing
%      singleton*.
%
%      H = MAQUINAMNPVI returns the handle to a new MAQUINAMNPVI or the handle to
%      the existing singleton*.
%
%      MAQUINAMNPVI('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in MAQUINAMNPVI.M with the given input arguments.
%
%      MAQUINAMNPVI('Property','Value',...) creates a new MAQUINAMNPVI or raises the
%      existing singleton*.  Star ting from the left, property value pairs are
%      applied to the GUI before MaquinaMNPVI_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to MaquinaMNPVI_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help MaquinaMNPVI

% Last Modified by GUIDE v2.5 16-Apr-2020 10:20:05

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @MaquinaMNPVI_OpeningFcn, ...
                   'gui_OutputFcn',  @MaquinaMNPVI_OutputFcn, ...
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


% --- Executes just before MaquinaMNPVI is made visible.
function MaquinaMNPVI_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to MaquinaMNPVI (see VARARGIN)

% Choose default command line output for MaquinaMNPVI
handles.output = hObject;

% Update handles structure
guidata(hObject, handles);
MyAtualizar(handles);

% UIWAIT makes MaquinaMNPVI wait for user response (see UIRESUME)
% uiwait(handles.figureMaquinaMNPVI);


% --- Outputs from this function are returned to the command line.
function varargout = MaquinaMNPVI_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;


% --- Executes on button press in pbAtualizar.
function pbAtualizar_Callback(hObject, eventdata, handles)
% hObject    handle to pbAtualizar (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
MyAtualizar(handles);


function eF_Callback(hObject, eventdata, handles)
% hObject    handle to eF (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of eF as text
%        str2double(get(hObject,'String')) returns contents of eF as a double


% --- Executes during object creation, after setting all properties.
function eF_CreateFcn(hObject, eventdata, handles)
% hObject    handle to eF (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function eA_Callback(hObject, eventdata, handles)
% hObject    handle to eA (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of eA as text
%        str2double(get(hObject,'String')) returns contents of eA as a double


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



function eB_Callback(hObject, eventdata, handles)
% hObject    handle to eB (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of eB as text
%        str2double(get(hObject,'String')) returns contents of eB as a double


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



function eN_Callback(hObject, eventdata, handles)
% hObject    handle to eN (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of eN as text
%        str2double(get(hObject,'String')) returns contents of eN as a double


% --- Executes during object creation, after setting all properties.
function eN_CreateFcn(hObject, eventdata, handles)
% hObject    handle to eN (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function eY0_Callback(hObject, eventdata, handles)
% hObject    handle to eY0 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of eY0 as text
%        str2double(get(hObject,'String')) returns contents of eY0 as a double


% --- Executes during object creation, after setting all properties.
function eY0_CreateFcn(hObject, eventdata, handles)
% hObject    handle to eY0 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end

% --- Motor da MáquinaMNPVI... .
function MyAtualizar(handles)
strF=get(handles.eF,'String');
f=@(t,y) eval(vectorize(strF));
a=str2num(get(handles.eA,'String'));
b=str2num(get(handles.eB,'String'));
n=str2num(get(handles.eN,'String'));
y0=str2num(get(handles.eY0,'String'));

escolhabg=get(handles.bgMetodos,'SelectedObject');
escolha=find([handles.rbEuler,...
              handles.rbEulerM,...
              handles.rbRK2,...
              handles.rbRK4,...
              handles.rbODE45,...
              handles.rbTodos]==escolhabg);
EULER=1; EULERM=2; RK2=3; RK4=4; ODE_45=5; TODOS=6;          

FTeste = MException('', 'Introduza uma função em t e y.');

try
    try
        fT = f(sym('t'), sym('y'));
        set(handles.eF, 'BackgroundColor', 'w');
    catch
         set(handles.eF, 'BackgroundColor', 'w');
         throw(FTeste);
         errordlg(Me.message);
    end
    
    yEuler=NEuler_v2(f,a,b,n,y0);
    yRK2=NRK2(f,a,b,n,y0);
    % outras funções/métodos

    sExata=dsolve(['Dy=',strF],['y(',num2str(a),')=',num2str(y0)]);
    g=@(t) eval(vectorize(char(sExata)));

    t=a:(b-a)/n:b;
    yExata=g(t);

    erroEuler=abs(yExata-yEuler);
    erroRK2=abs(yExata-yRK2);
    % outros erros
    
    plot(t,yExata,'-bo')
    hold on  
    
    switch escolha
        case EULER
            plot(t,yEuler,'-r*')
            legend('Exata','Euler')
            tabela=[t.',yExata.',yEuler.',erroEuler.'];
            set(handles.uiTabela,'Data',num2cell(tabela))
            set(handles.uiTabela,'ColumnName',...
                                 {'t','Exata','Euler','ErroEuler'})  
        case EULERM
            warndlg('Método não implementado','AVISO','modal')
        case RK2
            plot(t,yRK2,'-g+')
            legend('Exata','RK2')
            tabela=[t.',yExata.',yRK2.',erroRK2.'];
            set(handles.uiTabela,'Data',num2cell(tabela))
            set(handles.uiTabela,'ColumnName',...
                                 {'t','Exata','RK2','ErroRK2'})  
         case RK4
            warndlg('Método não implementado','AVISO','modal')
        case ODE_45
            warndlg('Método não implementado','AVISO','modal')
        case TODOS
            plot(t,yEuler,'-r*')
            plot(t,yRK2,'-g+')
            legend('Exata','Euler','RK2')  
            tabela=[t.',yExata.',yEuler.',yRK2.',erroEuler.',erroRK2.'];
            set(handles.uiTabela,'Data',num2cell(tabela))
            set(handles.uiTabela,'ColumnName',...
                {'t','Exata','Euler','RK2','ErroEuler','ErroRK2'})        
    end
    hold off
    grid on
catch Me
    errordlg(['Ocorreu um erro ',Me.message],'ERRO','modal')
end


% --------------------------------------------------------------------
function MFile_Callback(hObject, eventdata, handles)
% hObject    handle to MFile (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)


% --------------------------------------------------------------------
function MSair_Callback(hObject, eventdata, handles)
% hObject    handle to MSair (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
SN=questdlg('Quer mesmo sair?','Sair','Sim','Não','Sim');
if strcmp(SN,'Não')
    return;
end
delete(handles.figureMaquinaMNPVI)


% --- Executes when user attempts to close figureMaquinaMNPVI.
function figureMaquinaMNPVI_CloseRequestFcn(hObject, eventdata, handles)
% hObject    handle to figureMaquinaMNPVI (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: delete(hObject) closes the figure
%delete(hObject);
MSair_Callback([], [], handles)


% --- Executes on button press in cbGrelha.
function cbGrelha_Callback(hObject, eventdata, handles)
% hObject    handle to cbGrelha (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of cbGrelha

if get(hObject,'Value')
    grid on;
else
    grid off;
end
