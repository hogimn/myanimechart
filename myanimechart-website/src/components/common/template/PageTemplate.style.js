import styled from "styled-components";

export const BackgroundPage = styled.div`
  z-index: -1;
  background-color: rgb(27, 27, 27);
  min-height: 100vh;
`;

export const PageWrapper = styled.div`
  z-index: 1;
  background-color: #161616;
  color: white;
  margin-left: auto;
  margin-right: auto;
  width: 1400px;
  min-height: 100vh;
  @media (max-width: 1359px) {
    width: 100%;
  }
`;

export const MainWrapper = styled.main``;

export const Navigation = styled.nav`
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #2e51a2;
  font-weight: bold;
`;

export const MenuWrapper = styled.ul`
  z-index: 2;
  display: flex;
  list-style: none;
  padding: 0;
  margin: 0;
  font-size: 13px;
  position: relative;
`;

export const MenuItem = styled.li`
  position: relative;
  cursor: pointer;
  padding: 10px;

  &:hover {
    background-color: #333333;
  }

  &:hover > ul {
    display: block;
  }
`;

export const SubMenu = styled.ul`
  z-index: 3;
  display: none;
  position: absolute;
  top: 100%;
  left: 0;
  background-color: #3a3a3a;
  list-style: none;
  padding: 0;
  margin: 0;
  width: max-content;
  min-width: 175px;

  li {
    font-weight: normal;
    font-size: 11px;
    padding: 8px;
    cursor: pointer;

    &:hover {
      background-color: #f0f0f0;
      color: black;
    }
  }
`;

export const CurrentPagePanel = styled.div`
  border-bottom: 1px solid #555555;
  background-color: #222222;
  color: white;
  padding: 7px 10px;
  font-size: 15px;
  font-weight: bold;
`;
