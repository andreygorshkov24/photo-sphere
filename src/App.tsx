import { ThemesFolders } from './components/ThemesFolders.tsx';
import { Themes } from './components/Themes.tsx';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import './fontawesome.ts';

const App = () => {
  return (
    <div className="container">
      <ThemesFolders />
      <hr style={{ margin: '1.5rem 0 0 0' }} />
      <Themes />
    </div>
  );
};

export default App;
