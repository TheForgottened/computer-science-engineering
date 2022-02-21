function varargout = Fr2Vr_esquisso(varargin)
% FR2VR_ESQUISSO - Função real de duas variáveis reais
% 2ª parte de programa de AM2
% Licenciaturas em Eng. Informática
% 11ª semana de aulas
% 16/05/2016    Arménio Correia | armenioc@isec.pt
% FR2VR_ESQUISSO M-file for Fr2Vr_esquisso.fig
%      FR2VR_ESQUISSO, by itself, creates a new FR2VR_ESQUISSO or raises the existing
%      singleton*.
%
%      H = FR2VR_ESQUISSO returns the handle to a new FR2VR_ESQUISSO or the handle to
%      the existing singleton*.
%
%      FR2VR_ESQUISSO('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in FR2VR_ESQUISSO.M with the given input arguments.
%
%      FR2VR_ESQUISSO('Property','Value',...) creates a new FR2VR_ESQUISSO or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before Fr2Vr_esquisso_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to Fr2Vr_esquisso_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help Fr2Vr_esquisso

% Last Modified by GUIDE v2.5 17-May-2016 16:08:26

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @Fr2Vr_esquisso_OpeningFcn, ...
                   'gui_OutputFcn',  @Fr2Vr_esquisso_OutputFcn, ...
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


% --- Executes just before Fr2Vr_esquisso is made visible.
function Fr2Vr_esquisso_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to Fr2Vr_esquisso (see VARARGIN)

% Choose default command line output for Fr2Vr_esquisso
handles.output = hObject;

% Update handles structure
guidata(hObject, handles);
pbPlotAval_Callback([], [], handles);

% UIWAIT makes Fr2Vr_esquisso wait for user response (see UIRESUME)
% uiwait(handles.figure1);


% --- Outputs from this function are returned to the command line.
function varargout = Fr2Vr_esquisso_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;


% --- Executes on button press in pbPlotAval.
function pbPlotAval_Callback(hObject, eventdata, handles)
% hObject    handle to pbPlotAval (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)


% --- Executes on button press in checkboxGrelha.
function checkboxGrelha_Callback(hObject, eventdata, handles)
% hObject    handle to checkboxGrelha (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of checkboxGrelha



% --- Executes on button press in checkboxMP.
function checkboxMP_Callback(hObject, eventdata, handles)
% hObject    handle to checkboxMP (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of checkboxMP


% --- Executes on button press in pbReset.
function pbReset_Callback(hObject, eventdata, handles)
% hObject    handle to pbReset (see GCBO)
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



function eH1_Callback(hObject, eventdata, handles)
% hObject    handle to eH1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of eH1 as text
%        str2double(get(hObject,'String')) returns contents of eH1 as a double


% --- Executes during object creation, after setting all properties.
function eH1_CreateFcn(hObject, eventdata, handles)
% hObject    handle to eH1 (see GCBO)
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



function eC_Callback(hObject, eventdata, handles)
% hObject    handle to eC (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of eC as text
%        str2double(get(hObject,'String')) returns contents of eC as a double


% --- Executes during object creation, after setting all properties.
function eC_CreateFcn(hObject, eventdata, handles)
% hObject    handle to eC (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end

function eH2_Callback(hObject, eventdata, handles)
% hObject    handle to eH2 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of eH2 as text
%        str2double(get(hObject,'String')) returns contents of eH2 as a double


% --- Executes during object creation, after setting all properties.
function eH2_CreateFcn(hObject, eventdata, handles)
% hObject    handle to eH2 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end

function eD_Callback(hObject, eventdata, handles)
% hObject    handle to eD (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of eD as text
%        str2double(get(hObject,'String')) returns contents of eD as a double


% --- Executes during object creation, after setting all properties.
function eD_CreateFcn(hObject, eventdata, handles)
% hObject    handle to eD (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
