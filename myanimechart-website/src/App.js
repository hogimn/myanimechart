import "./App.css";
import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import AnimeStat from "./components/pages/animestat/AnimeStat";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path={"/"} element={<Navigate to={"/animeStat"} replace />}/>
                <Route path={"/animeStat"} element={<AnimeStat/>}/>
            </Routes>
        </BrowserRouter>
    );
}

export default App;
