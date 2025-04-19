/* Rutinas JAVA Script  - Carga una imagen local en la pagina*/
function readURL(input){
    if(input.files && input.files[0]){
        var lector = new FileReader();
        lector.onload = function(e) {
            $('#blah').attr('src',e.target.result).height(200);
        };   
        lector.readAsDataURL(input.files[0]);
    }
}





