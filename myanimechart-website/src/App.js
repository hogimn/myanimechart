import "./App.css";
import { HashRouter, Navigate, Route, Routes } from "react-router-dom";
import SeasonalAnime from "./components/pages/seasonalanime/SeasonalAnime";
import { registerCharts } from "./components/common/chart/chartRegistration";

registerCharts();

function App() {
  return (
    <HashRouter>
      <Routes>
        <Route
          path={"/"}
          element={<Navigate to={"/seasonal-anime"} replace />}
        />
        <Route path={"/seasonal-anime"} element={<SeasonalAnime />} />
      </Routes>
    </HashRouter>
  );
}

export default App;
