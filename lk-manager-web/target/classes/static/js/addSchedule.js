


        document.addEventListener('DOMContentLoaded', function() {
               const tableBody = document.querySelector('#options-table tbody');
               let draggedRow = null;


               const dragHandles = document.querySelectorAll('.draggable');
               dragHandles.forEach(dragHandle => {
                   dragHandle.addEventListener('dragstart', function(e) {
                       draggedRow = this.parentElement; // dragging row
                       e.dataTransfer.effectAllowed = 'move';
                       e.dataTransfer.setData('text/html', this.innerHTML);
                   });

                   dragHandle.addEventListener('dragover', function(e) {
                       e.preventDefault();
                       e.dataTransfer.dropEffect = 'move';
                   });

                   dragHandle.addEventListener('drop', function(e) {

                       if (draggedRow) {

                           e.preventDefault();

                           if (this.parentElement !== draggedRow) {

                               tableBody.insertBefore(draggedRow, this.parentElement);

                           }
                           draggedRow = null;
                       }
                   });
               });
           });

















//*************************************************************


/*
function dodajTerminarzPojedynczy(){

alert("a.getAttribute("th:field")");
var a=document.getElementById('mecze')
a.setAttribute("th:field",matchMap )
alert(a.getAttribute("th:field"));



}

*/



 let  selection1=  document.getElementById('selection1')
  let  selection2=  document.getElementById('selection2')

/*  var cc = [[${schedule.playersList}]];*/


 let selectionOptionsBackup = document.getElementById('selection1').cloneNode( true );

//let matchMap= new Map();
let  removedOptions=[];




//let matchPairs=Array.from(playersList)




function undo()
{



/*tablica.pop()
tablica.pop()*/








let mecze=document.getElementById('matches');
mecze.lastChild.remove();

console.log(removedOptions)
  let player1=removedOptions.pop()//.at(length-1);
  let player2 = removedOptions.pop()//.at(length-2)

  //console.log("p1" +player1)
 //  console.log("p2"+player2)
console.log("del "+player1 + " i " + player2)
   if(player1!=undefined && player2 != undefined)
   {
    let option1 = document.createElement("option");
     option1.value=player1;
      let text1 = document.createTextNode(player1 );
     option1.appendChild(text1)

      let option2 = document.createElement("option");
     option2.value=player2;
      let text2 = document.createTextNode(player2 );
     option2.appendChild(text2)

       selection1.appendChild(option1)
   selection1.appendChild(option2)

     let  option3 = document.createElement("option");
     option3.value=player1;
       text3 = document.createTextNode(player1 );
     option3.appendChild(text3)

       option4 = document.createElement("option");
     option4.value=player2;
    text4 = document.createTextNode(player2 );
     option4.appendChild(text4)


     selection2.appendChild(option3)
     selection2.appendChild(option4)

   //console.log(player1)
  // console.log("r1 " +removedOptions)
    //removedOptions=removedOptions.filter( option=> option!=player1 && option!=player2 );
 //  console.log(removedOptions.pop()) ;
 //  console.log(removedOptions.pop()) ;


    removedOptions.forEach( a=> console.log(a==player1))

 //  console.log("r2 " +removedOptions)
   //console.log(selection2)
    // console.log(selection2.children)
     sortSelection(selection1)
    sortSelection(selection2)


    let matchPairs=document.getElementById('playersListInput');
           playersList.pop()
           playersList.pop()
           matchPairs.setAttribute("value", playersList )
console.log(matchPairs.value)
   }


/*//
let matchPairs=document.getElementById('playersListInput');
//alert(tablicaWartosci.size);
console.log(matchPairs.value)

 matchPairs.setAttribute("value", matchPairs )*/

}


function clearAll()
{

let matchPairs=document.getElementById('playersListInput');

/*
//alert(playerNames);
//alert(tablicaWartosci1.size);
let tablica=[]
alert(matchPairs.value)
matchPairs.setAttribute("value", tablica )
//alert(tablicaWartosci1.length);
 // players=[]
alert(matchPairs.value)*/



// let matchPairs=document.getElementById('playersListInput');
 while(playersList.length!=0)
 {  playersList.pop()
 }


         //  playersList.pop()
           matchPairs.setAttribute("value", playersList )
console.log(matchPairs.value)



  //  const selectionBackup=document.getElementById('selection1') ;

    let  selection1=  document.getElementById('selection1')
    let  selection2=  document.getElementById('selection2')

    selectionBackup2= selectionOptionsBackup.cloneNode( true );
     selectionBackup1= selectionOptionsBackup.cloneNode( true );
  //  document.getElementById('selection1').
  //  document.getElementById('selection1').childNodes.appendChild(selectionBackup.childNodes)
  selection1.  replaceChildren(...selectionBackup1.children)
  selection1[0].selected=true;
  selection2.  replaceChildren(...selectionBackup2.children)
  selection2[0].selected=true;
 // selectionBackup=null;

//alert("i");
  document.getElementById('matches').innerHTML=""

}


function scheduleChange(value)
{
   var schedule1=  document.getElementById("singleSchedule")
   var schedule2=  document.getElementById("multiSchedule")
     var roundsNumber= document.getElementById("roundsNumber")
     var matches =  document.getElementById('matches').innerHTML="";

    if(value=='oneDaySchedule')
    {
schedule1.hidden=false;
schedule2.hidden=true;
matches.hidden=false;
   roundsNumber.hidden=true;
    }
    else if(value=='swissSchedule')
    {
    schedule1.hidden=true;
    schedule2.hidden=false;

     roundsNumber.hidden=false;
matches.hidden=true;
    }
    else//multi schedule
    {
schedule1.hidden=true;
schedule2.hidden=false;


   roundsNumber.hidden=true;
   matches.hidden=true;
    }

}


function add()
   {




///////////////////////////
/*    //bkp
var bb = [[${schedule}]];

var  listFragenText= new Map(Object.entries(bb));
//.bob.bobby

//bb.setNazwa("jjjjjjj")
//listFragenText.set('kot','pat')
listFragenText.nazwa="nazwa"
var t=listFragenText.get("nazwa")

bb.bob.bobby=listFragenText;

var f = JSON.stringify(bb.bob)
alert(  bb.bob.bobby);
alert(  t);
*/
/*  //bkp


var bb = [[${schedule}]];

var  listFragenText= new Map(Object.entries(bb.mapa))
//.bob.bobby

//bb.setNazwa("jjjjjjj")

listFragenText.set('kot','pat')
//listFragenText.nazwa="nazwa"
//var t=listFragenText.get("nazwa")
alert( "bb.mapa="+ bb.mapa);
alert( "listFragenText="+ listFragenText);
bb.mapa=listFragenText;
alert( "bb.kot="+ bb.mapa.get("bob"));
var f = JSON.stringify(listFragenText)
alert( "y" +f);

*/



/*
   tempstring="";
    for (const [key, value] of Array.from(listFragenText)) {
        tempstring+= key+"-"
        tempstring+= value

    };
   Array.from (listFragenText).forEach(element => {
   tempstring+=element;
});

*/

//alert(  listFragenText.get('bob'));




/*  if(selectionBackup==null)
  {
   // selectionBackup= structuredClone(selection1)
 //   selectionBackup = document.getElementById('selection1').cloneNode( true );
  //  selectionBackup = JSON.parse(JSON.stringify(selection1));
    // selectionBackup = document.getElementById('selection1')
  }*/
/*
if(gracze1==null)
{
    gracze1=document.getElementById('selection1')
}
*/

  let user1,user2, index1,index2;

  Array.from(selection1).forEach(element => {
 /*   if(
        (element.selected==true)
    &&(element.text!='&#45;&#45;Wybierz terminarz&#45;&#45;' )
    )
    {
        user1=element;
        index1=element.index;
    }*/
if(
        (element.selected==true)

    )
    {
        user1=element;
        index1=element.index;
    }
});
Array.from(selection2).forEach(element => {
   /* if(element.selected==true&&element.text!='&#45;&#45;Wybierz terminarz&#45;&#45;'&&element.index!=index1)
    {
        user2=element;
        index2=element.index;
    }*/
  if(element.selected==true)
    {
        user2=element;
        index2=element.index;
    }
});


/*
let tablica=Array.from(playersList);*/
//alert(tablica);
/*alert("user2:" + user2.value);
alert("plyers:" + playerNames);
alert("in1:" +  playerNames.includes(user1.value));
alert("in2:" +  playerNames.includes(user2.value));*/
if(user1==null||user2==null)
{ alert("Musisz podać graczy");
}
else
{
    if(!playerNames.includes(user1.value) || !playerNames.includes(user2.value))
            {
            alert("Musisz wybrać parę graczy");
            }

    else
    {
         if(user1.value==user2.value )
            {
            alert("Gracz nie moze grac ze soba");
            }
        else
        {

        //alert("1");

        //alert("2");

/*

        tablica.push(user1.text)
        tablica.push(user2.text)
*/

 // matchMap.set(user1.text,user2.text)


        //alert("3");
       let matchPairs=document.getElementById('playersListInput');
       playersList.push(user1.text)
       playersList.push(user2.text)
       matchPairs.setAttribute("value", playersList )
        //alert("4");
//      s.playersList.push(user1.text)
  //    s.playersList.push(user2.text)
//console.log(s.playersList)

//matchPairs.setAttribute("value", matchMap)

        let matches=document.getElementById('matches');

        var pair = document.createElement("div")

        var node = document.createTextNode(user1.text +" - "+user2.text );

        pair.appendChild(node)
        matches.appendChild(pair)
        //mecze.children.appendChild=pair;

         //   gracze1.remove(user1)
         //   gracze1.remove(user2)

         Array.from(selection1).forEach(element => {
            if(element.text==user2.text)
            {
               element.remove();

            }

        });
        Array.from(selection2).forEach(element => {
            if(element.text==user1.text)
            {
               element.remove();

            }

        });


  removedOptions.push(user1.value);
  removedOptions.push(user2.value);








        /*
        var a=document.getElementById('bob')
        //alert(bobby.bobby.get('piko66'));
        //a.setAttribute("th:field", listFragenText )
        a.setAttribute("value", listFragenText )
        */















        //cc.put("kot","pat")
        /*
        var bob1=document.getElementById('bob1')

        var  kk= new Map(Object.entries(cc))
        kk.set("kot","pat")
        var fff = JSON.stringify(kk)
        alert(fff);
        //bb.mapa.get("kot"));
        //kk.set(user1.text,user2.text)
        //alert(listFragenText);
        kk.set(user1.text,user2.text)



        //bb.mapa=listFragenText
        var ff = JSON.stringify(cc.mapa)
        bob1.setAttribute("value", ff )
        alert('f'+ff);


        alert("bob1="+ bob1.getAttribute('value'));

        */





        /////////////////////////////////////////////

        user1.remove();
        user2.remove();



        //    alert(tempstring);

        }

    }

}









   }





function sortSelection(selection) {
    var tempArray = new Array();
    for (var i=0;i<selection.options.length;i++) {
        tempArray[i] = new Array();
        tempArray[i][0] = selection.options[i].text;
        tempArray[i][1] = selection.options[i].value;
    }
    tempArray.sort();
//     while (selection.options.length > 0) {
//       console.log(  selection.options[0].text)
//         selection.options[0] = null;
//     }
    for (var i=0;i<tempArray.length;i++) {
        var option = new Option(tempArray[i][0], tempArray[i][1]);
        selection.options[i] = option;
    }
    return;
}












