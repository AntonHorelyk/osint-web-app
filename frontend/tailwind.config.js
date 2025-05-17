/** @type {import('tailwindcss').Config} */
module.exports = {
    content: ['./src/**/*.{js,ts,jsx,tsx,html}'],
    theme: {
      extend: {
        colors: {
          primary: '#ECFAE5',         // например, темно-серый
          secondary: '#B2D3C2',       // ваш второй цвет
          'secondary-dark': '#94B4C1' // чуть темнее для hover
        },
        borderRadius: {
          '2xl': '1rem',
        },
      },
    },
    plugins: [],
  }