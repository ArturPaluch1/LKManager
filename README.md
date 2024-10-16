# LKManager



Program wykonany z użyciem Spring Boot, umożliwia tworzenie i łatwe zarządzanie rozgrywkami klanowymi opartymi na meczach towarzyskich w managerze internetowym <a href ="https://www.managerzone.com/">Managerzone</a>.
<details >
 <summary>Managerzone</summary>
<a href="https://ibb.co/Fw73RyL"><img src="https://i.ibb.co/fp0Fjyz/mz.png" alt="mz" border="0" /></a>
</details>
Główna baza danych (SQL) znajduje sę na serwerze. Dodatkowo, żeby usprawnić działanie programu (zapytania sql na serwerze produkcyjnym realizowane są z lekkim opóźnieniem) część najczęściej używanych danych (lista użytkowników, tabela, terminarz) są zapisywane także w bazie danych no-sql Redis. Ostatnio odwiedzony terminarz i runda zapisywane są w plikach cookie, aby w razie większej ilości terminarzy użytkownik nie był pogubiony.
Rozgrywki odbywają się co wtorek. Codziennie program sprawdza, czy jest zaplanowana kolejny terminarz ligowy, jeśli nie to go tworzy. Sprawdza czy obecna liga dobiegłą końca, jeśli tak to zaplanowany terminarz uzupełnia rundami stworzonymi dla graczy którzy wcześniej się zapisali się do gry. We wtorek o 10:55, 11:55, 19:55, 20:55 i 23:55 podejmowane są próby pobrania wyników meczów z MZ (api Managerzone pozwala pobierać wyniki w formacie XML), następnie uzupełniana jest runda i zapisywana w bazie danych. Przy każdej aktualizacji wyników tworzona jest też aktualna tabela i zapisywana w bazie Redis. Użytkownicy mogą je przeglądać na stronie HTML. 

Serwis jest ze względu na 3 rodzaje użytkowników. Niezalogowanych, zalogowanych i admina.

<details >
 <summary>Niezalogowany użytkownik może przeglądać stronę główną:</summary>
<a href="https://ibb.co/T0Cr9CZ"><img src="https://i.ibb.co/M1X9FXb/Screenshot-2024-10-17-at-00-01-05-LK-Manager.png" alt="Screenshot-2024-10-17-at-00-01-05-LK-Manager" border="0"></a>

</details>
<details >
 <summary>Przeglądać tabele:
</summary>
<a href="https://ibb.co/51tQdsf"><img src="https://i.ibb.co/6yVK2Df/Screenshot-2024-10-17-at-00-02-23-Title.png" alt="Screenshot-2024-10-17-at-00-02-23-Title" border="0"></a>

</details>
<details >
 <summary>I wyniki:
</summary>
<a href="https://ibb.co/T4rX2wd"><img src="https://i.ibb.co/BywD3fF/Screenshot-2024-10-17-at-00-02-37-Navbar.png" alt="Screenshot-2024-10-17-at-00-02-37-Navbar" border="0"></a>

</details>
<details >
 <summary>A także założyć konto:
</summary>
<a href="https://ibb.co/JH16rkN"><img src="https://i.ibb.co/QMgSYmZ/Screenshot-2024-10-17-at-00-02-49-Sign-up.png" alt="Screenshot-2024-10-17-at-00-02-49-Sign-up" border="0"></a>

</details>

<details >
 <summary>Albo się zalogować:
</summary>
<a href="https://ibb.co/ZgX7pYP"><img src="https://i.ibb.co/bvmDM2Z/Screenshot-2024-10-17-at-00-02-59-Log-in.png" alt="Screenshot-2024-10-17-at-00-02-59-Log-in" border="0"></a>

</details>

<details >
 <summary>Zalogowany użytkownik ma dodatkowo możliwość ustawić swoje konto:
</summary>
<a href="https://ibb.co/WVsnkqP"><img src="https://i.ibb.co/9ZGnbzV/Screenshot-2024-10-17-at-00-06-34-Ustawienia.png" alt="Screenshot-2024-10-17-at-00-06-34-Ustawienia" border="0"></a>

</details>
<details >
 <summary>Ustawić nazwę swojego konta w MZ, które będzie uwzględniane do tworzenia terminarzy:
</summary>
<a href="https://ibb.co/8d5HjDQ"><img src="https://i.ibb.co/61Pk4Rq/Screenshot-2024-10-17-at-00-06-51-Ustawienia.png" alt="Screenshot-2024-10-17-at-00-06-51-Ustawienia" border="0"></a>

</details>
Po dodaniu konta MZ użytkownik dostaje możliwość dołączenia do przyszłej ligi, ją zasubskrybować do odwołania, albo się wypisać.

<details >
 <summary>Ustawić email, żeby móc przypomnieć swoje hasło (jeszcze niedostępna opcja). Po wpisaniu emaila dostaje mail z linkiem potwierdzającym prawdziwość maila.
</summary>
<a href="https://ibb.co/qysT8Qm"><img src="https://i.ibb.co/p0zYTF4/Screenshot-2024-10-17-at-00-07-09-Ustawienia.png" alt="Screenshot-2024-10-17-at-00-07-09-Ustawienia" border="0"></a>

</details>

<details >
 <summary>Admin poza opcjami zalogowanego użytkownika może przeglądać zarejestrowanych użytkowników, dodawać nowych (głównie było wykorzystywane do tworzenia użytkowników tekstowych zanim poja wiła się opcja tworzena konta) i usuwać.
</summary>
<a href="https://ibb.co/D59cwpZ"><img src="https://i.ibb.co/syKxsHZ/Screenshot-2024-10-17-at-00-11-46-Navbar.png" alt="Screenshot-2024-10-17-at-00-11-46-Navbar" border="0"></a>

</details>

<details >
 <summary>W zakładce wyniki może bezpośrednio (poza zaprogramowanym automatycznym zbieraniem wyników o konkretnych godzinach) aktualizować wyniki. 
</summary>
<a href="https://ibb.co/cDzrG16"><img src="https://i.ibb.co/6g6W3mH/Screenshot-2024-10-17-at-00-08-52-Navbar.png" alt="Screenshot-2024-10-17-at-00-08-52-Navbar" border="0"></a>

</details>
<details >
 <summary>A także je edytować.
</summary>
<a href="https://ibb.co/KDcGmNH"><img src="https://i.ibb.co/Vx0N3H8/Screenshot-2024-10-17-at-00-09-06-Navbar.png" alt="Screenshot-2024-10-17-at-00-09-06-Navbar" border="0"></a>

</details>
<details >
 <summary>Zakładka terminarz pozwala przeglądać stworzone terminarze.
</summary>
<a href="https://ibb.co/cx88k6F"><img src="https://i.ibb.co/kMDD548/Screenshot-2024-10-17-at-00-13-33-Title.png" alt="Screenshot-2024-10-17-at-00-13-33-Title" border="0"></a>

</details>
<details >
 <summary>Usuwać je:
</summary>
<a href="https://ibb.co/F3gYtDc"><img src="https://i.ibb.co/kBQDFSz/Nowy-obraz-mapy-bitowej.png" alt="Nowy-obraz-mapy-bitowej" border="0"></a>

</details>
<details >
 <summary>I dodawać nowe - 3 rodzaje, jednodniowy:
</summary>
<a href="https://ibb.co/cx3Wwjh"><img src="https://i.ibb.co/ZNXQS0z/jednodniowy.png" alt="jednodniowy" border="0"></a>

</details>
<details >
 <summary>Wielodniowy:
</summary>
<a href="https://ibb.co/rbZy8wN"><img src="https://i.ibb.co/M8RNwCQ/Screenshot-2024-10-17-at-00-17-26-dodawanie-terminarza.png" alt="Screenshot-2024-10-17-at-00-17-26-dodawanie-terminarza" border="0"></a>

</details>
Ta opcja tworzy terminarz uwzględniający wszystkich wskazanych graczy 

<details >
 <summary>W systemie szwajcarskim:
</summary>
<a href="https://ibb.co/Jc7XX06"><img src="https://i.ibb.co/6yP66V9/Screenshot-2024-10-17-at-00-17-47-dodawanie-terminarza.png" alt="Screenshot-2024-10-17-at-00-17-47-dodawanie-terminarza" border="0"></a>

</details>
W tym rodzaju tworzona jest tylko pierwsza runda rozgrywek, a kolejne są tworzone zgodnie z harmonogramem dzień po rozegraniu poprzedniej kolejki. Kiedy dzień meczowy mija program oblicza na podstawie sum punktów graczy z wcześniej rozegranych kolejek tabelę, a następnie tworzy pary na następny tydzień. Gracz z największą liczbą punktów gra z kolejnym, i tak dalej, przy czym jeśli dana para już wystąpła, to dobierana jest para z większą różnicą punktów między graczami.




