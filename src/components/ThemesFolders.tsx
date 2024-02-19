import { DragEvent, useEffect, useRef, useState } from 'react';
import { TTheme, TThemeFolder } from '../model.ts';
import { addThemeToThemeFolder, fetchThemes, fetchThemesFolders, themeDown, themeUp } from '../server_api.ts';
import { ThemeFolder } from './ThemeFolder.tsx';
import { Theme } from './Theme.tsx';
import { Position, Positionable } from './utils/Positionable.tsx';

const ThemesFolders = () => {
  const [themesFolders, setThemesFolders] = useState<TThemeFolder[]>([]);
  const [selectedThemeFolder, setSelectedThemeFolder] = useState<TThemeFolder | null>(null);
  const [selectedThemeFolderThemes, setSelectedThemeFolderThemes] = useState<TTheme[]>([]);

  useEffect(() => {
    void (async () => {
      try {
        const themesFolders = await fetchThemesFolders();
        setThemesFolders(themesFolders);
      } catch (e: unknown) {
        console.error(e);
        setThemesFolders([]);
      }
    })();
  }, []);

  const refreshSelectedThemeFolder = async (themeFolder: TThemeFolder) => {
    setSelectedThemeFolder(themeFolder);
    await refreshSelectedThemeFolderThemes(themeFolder);
  };

  const refreshSelectedThemeFolderThemes = async (themeFolder: TThemeFolder | null = selectedThemeFolder) => {
    if (!themeFolder) {
      setSelectedThemeFolderThemes([]);
      return;
    }

    try {
      setSelectedThemeFolderThemes(await fetchThemes(themeFolder.id));
    } catch (e: unknown) {
      console.error(e);
      setSelectedThemeFolderThemes([]);
    }
  };

  const targetThemeFolderElement = useRef<HTMLDivElement | null>();
  const targetThemeFolder = useRef<TThemeFolder | null>();
  const targetTheme = useRef<TTheme | null>();

  const handleOnDragOver = (e: DragEvent<HTMLDivElement>, themeFolder: TThemeFolder) => {
    targetThemeFolderElement.current = e.currentTarget;
    targetThemeFolder.current = themeFolder;
  };

  const handleOnDragStart = (theme: TTheme) => targetTheme.current = theme;
  const handleOnDragEnd = async (e: DragEvent<HTMLDivElement>) => {
    if (targetThemeFolderElement.current) {
      const boundingClientRect = targetThemeFolderElement.current.getBoundingClientRect();
      const isXValid = e.clientX > boundingClientRect.x && e.clientX < boundingClientRect.x + boundingClientRect.width;
      const isYValid = e.clientY > boundingClientRect.y && e.clientY < boundingClientRect.y + boundingClientRect.height;
      if (isXValid && isYValid && targetThemeFolder.current && targetTheme.current) {
        try {
          await addThemeToThemeFolder(targetTheme.current.id, targetThemeFolder.current.id);
        } catch (e: unknown) {
          console.error(e);
        } finally {
          targetThemeFolderElement.current = null;
          targetThemeFolder.current = null;
          targetTheme.current = null;
        }
        await refreshSelectedThemeFolderThemes(selectedThemeFolder);
      }
    }
  };

  const handleOnClickUp = async (theme: TTheme) => {
    try {
      await themeUp(theme.id);
      await refreshSelectedThemeFolderThemes();
    } catch (e: unknown) {
      console.error(e);
    }
  };

  const handleOnClickDown = async (theme: TTheme) => {
    try {
      await themeDown(theme.id);
      await refreshSelectedThemeFolderThemes();
    } catch (e: unknown) {
      console.error(e);
    }
  };

  return (
    <div className="row">
      <div className="col-6 p-0">
        <Positionable position={Position.End}>
          <h3>ThemeFolders</h3>
        </Positionable>
        {!!themesFolders && themesFolders.map((themeFolder, key) => {
          return (
            <div key={key} onClick={() => refreshSelectedThemeFolder(themeFolder)} onDragOver={(e) => handleOnDragOver(e, themeFolder)}>
              <ThemeFolder {...themeFolder} position={Position.Start} onClickUp={null} onClickDown={null} />
            </div>
          );
        })}
      </div>
      <div className="col-6 p-0">
        {!!selectedThemeFolder ? (
          <>
            <Positionable position={Position.End}>
              <h3>Name: {selectedThemeFolder.name}</h3>
              <span className="text-secondary">Description: {selectedThemeFolder.description}</span>
              <hr />
              <span className="text-secondary">Themes count: {selectedThemeFolderThemes.length}</span>
            </Positionable>
            {selectedThemeFolderThemes.map((theme, key) => {
              return (
                <div key={key} onDragStart={() => handleOnDragStart(theme)} onDragEnd={(e) => handleOnDragEnd(e)} draggable>
                  <Theme {...theme} position={Position.End} onClickUp={() => handleOnClickUp(theme)} onClickDown={() => handleOnClickDown(theme)} />
                </div>
              );
            })}
          </>
        ) : (
          <Positionable position={Position.End}>
            <h3>No selected theme folder...</h3>
          </Positionable>
        )}
      </div>
    </div>
  );
};

export { ThemesFolders };
