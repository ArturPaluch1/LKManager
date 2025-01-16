


function updateClick()
{
 //alert("tabela");
var form  = document.getElementById('formUpdate');
//document.getElementByClassName("formAktualizuj")[0];
//var runda = [[${mecze}]];

 //alert("form");
form.submit()
 //alert("form end");

}









function showEditTable()
{
var editTable=document.getElementById("editTable");
var table=document.getElementById("table");

table.hidden=true;

editTable.hidden=false;
}

function cancel(){

var editTable=document.getElementById("editTable");
var table=document.getElementById("table");



editTable.hidden=true;
table.hidden=false;



}



function saveChanges()
{
var editTable=document.getElementById("editTable");
var table=document.getElementById("table");

//tabela.hidden=false;

//edytujTabele.hidden=true;

 //alert(tabela);

 var editTableContent =document.getElementById("editTableContent");
//alert(editTableContent.children)

//0 team1 1 u1 9 u2 10t2
 //console.log(editTableContent.children[0].children)
 //alert(editTableContent);


var editTableForm=document.getElementById("editTableForm");
editTableForm.submit();

}


/*


const round = [[${chosenSchedule}]];
function aktualizujClick(){
var form = document.getElementById("formAktualizuj");

alert("round");
if(round.status=="rozegrana")
{
if (confirm('Ta round była już aktualizowana. Na pewno chcesz ją nadpisać?')) {
  // Save it!
  form.submit()
  alert('aktualizuje');
} else {
  // Do nothing!
 alert('anulowano.');
}


}

else
{
  form.submit()
}





}




*/








      function copy() {
        var result = "";
     var tempString = "";
     var tempResults = "";

     var table = document.getElementById("table-content");
var date = document.getElementById("matchDate");
var round =  document.getElementById("round");
tempString+=" Runda: "+round.textContent +"   "+date.textContent+"\r\n";


 //console.log(date+ date.textContent);




    // let pierwszy = true;
     for (let i in table.rows) {
       let row = table.rows[i];



       for (let j in row.cells) {
         let col = row.cells[j];

  //    console.log(j + "->" + row.cells[j].textContent);

 var results;
 var user2;
// console.log(j );
         switch (j) {
           //druzyna
           case "0": {
          results="";
 user2="";
             tempString += row.cells[j].textContent;
             break;
           }
                        //user
           case "1": {
             tempString += " (" + row.cells[j].textContent + ") - ";
             break;
           }
           case "2": {
           results+=row.cells[j].textContent + ":"
           //  tempString += row.cells[j].textContent + ":";
             break;
           }
           case "4": {
            results+= row.cells[j].textContent + "   ";
             //tempString += row.cells[j].textContent + "   ";
             break;
           }
           case "6": {
           results+=row.cells[j].textContent + ":"
             //tempString += row.cells[j].textContent + ":";
             break;
           }
           case "8": {
           results+=row.cells[j].textContent + " " + "\r\n";
           //  tempString += row.cells[j].textContent + " " + "\r\n";
             break;
           }
             //user
           case "9": {
           user2+=" (" + row.cells[j].textContent  + ") ";
        //     tempString +=" (" + row.cells[j].textContent  + ") ";
             break;
           }
             //druzyna
           case "10": {

             tempString +=  row.cells[j].textContent+user2+results;
                        results="";
 user2="";
             break;
           }
         }


       }


     }
     tempString += "\r\n";

  // console.log(tempString);
   navigator.clipboard.writeText(tempString);
   alert("Copied the text: " + tempString);


}








const alertElement = document.getElementById("seasonEndAlert");


    const today = new Date();

      const endDateString = alertElement.getAttribute("data-end-date");

     const endDate  =new Date( endDateString);
const endDateMinus15= new Date(endDate);
 endDateMinus15.setDate(endDateMinus15.getDate() - 15);


const leagueParticipation = alertElement.getAttribute("data-league-participation");




    if (today > endDateMinus15 && today<= endDate ) {

     console.log(leagueParticipation);



   switch(leagueParticipation)
    {
    case "UNSIGNED":
    {
    alertElement.innerText = "Nie jesteś zapisany do kolejnego sezonu, a obecny kończy się. Przypominam, że zapisy do następnego trwają cały czas podczas rozgrywania bieżącego.";

break;
    }
      case "SIGNED":
    {
    alertElement.innerText = "Kończy się sezon. Jesteś zapisany do następnego.";
alertElement.style.backgroundColor = "lightgreen";
alertElement.style.color = "darkgreen";
break;
    }
      case "SUBBED":
    {
   alertElement.innerText = "Kończy się sezon. Jesteś zapisany do kolejnych.";
    alertElement.style.backgroundColor = "lightgreen";
    alertElement.style.color = "darkgreen";
break;
    }
    default:
        {

    alertElement.innerText = "Kończy się sezon. Przypominam, że zapisy do następnego trwają cały czas podczas rozgrywania bieżącego.";
break;
    }
    }


        }



 if (!alertElement.innerText) {
        alertElement.style.display = 'none';
    }
else {
        alertElement.style.display = 'block';
    }











