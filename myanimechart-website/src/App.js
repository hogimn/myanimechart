import "./App.css";
import {HashRouter, Navigate, Route, Routes} from "react-router-dom";
import AnimeStat from "./components/pages/animestat/AnimeStat";

function App() {
    return (
        <HashRouter>
            <Routes>
                <Route path={"/"} element={<Navigate to={"/animeStat"} replace />}/>
                <Route path={"/animeStat"} element={<AnimeStat/>}/>
            </Routes>
        </HashRouter>
    );
}

export default App;
