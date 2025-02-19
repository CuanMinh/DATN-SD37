USE [master]
GO
/****** Object:  Database [datntryonall]    Script Date: 03/02/2025 12:42:29 am ******/
CREATE DATABASE [datntryonall]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'datntryonall', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.THANGBDPH19964\MSSQL\DATA\datntryonall.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'datntryonall_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.THANGBDPH19964\MSSQL\DATA\datntryonall_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT, LEDGER = OFF
GO
ALTER DATABASE [datntryonall] SET COMPATIBILITY_LEVEL = 160
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [datntryonall].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [datntryonall] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [datntryonall] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [datntryonall] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [datntryonall] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [datntryonall] SET ARITHABORT OFF 
GO
ALTER DATABASE [datntryonall] SET AUTO_CLOSE ON 
GO
ALTER DATABASE [datntryonall] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [datntryonall] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [datntryonall] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [datntryonall] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [datntryonall] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [datntryonall] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [datntryonall] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [datntryonall] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [datntryonall] SET  ENABLE_BROKER 
GO
ALTER DATABASE [datntryonall] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [datntryonall] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [datntryonall] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [datntryonall] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [datntryonall] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [datntryonall] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [datntryonall] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [datntryonall] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [datntryonall] SET  MULTI_USER 
GO
ALTER DATABASE [datntryonall] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [datntryonall] SET DB_CHAINING OFF 
GO
ALTER DATABASE [datntryonall] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [datntryonall] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [datntryonall] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [datntryonall] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
ALTER DATABASE [datntryonall] SET QUERY_STORE = ON
GO
ALTER DATABASE [datntryonall] SET QUERY_STORE (OPERATION_MODE = READ_WRITE, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), DATA_FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_STORAGE_SIZE_MB = 1000, QUERY_CAPTURE_MODE = AUTO, SIZE_BASED_CLEANUP_MODE = AUTO, MAX_PLANS_PER_QUERY = 200, WAIT_STATS_CAPTURE_MODE = ON)
GO
USE [datntryonall]
GO
/****** Object:  Table [dbo].[chat_lieu]    Script Date: 03/02/2025 12:42:29 am ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[chat_lieu](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[ten] [nvarchar](100) NULL,
	[mo_ta] [nvarchar](255) NULL,
	[ngay_tao] [datetime] NULL,
	[ngay_cap_nhap] [datetime] NULL,
	[trang_thai] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[chi_tiet_san_pham]    Script Date: 03/02/2025 12:42:29 am ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[chi_tiet_san_pham](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[trong_luong] [float] NULL,
	[gia] [money] NULL,
	[giam_gia] [money] NULL,
	[mo_ta] [nvarchar](255) NULL,
	[so_luong] [int] NULL,
	[ngay_tao] [datetime] NULL,
	[ngay_cap_nhap] [datetime] NULL,
	[thoi_gian_giam_gia] [datetime] NULL,
	[trang_thai] [int] NULL,
	[san_pham_id] [bigint] NULL,
	[mau_sac_id] [bigint] NULL,
	[kich_co_id] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[danh_muc]    Script Date: 03/02/2025 12:42:29 am ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[danh_muc](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[ten] [nvarchar](100) NULL,
	[mo_ta] [nvarchar](255) NULL,
	[ngay_tao] [datetime] NULL,
	[ngay_cap_nhap] [datetime] NULL,
	[trang_thai] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[dia_chi]    Script Date: 03/02/2025 12:42:29 am ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[dia_chi](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[ten] [nvarchar](100) NULL,
	[so_dien_thoai] [varchar](15) NULL,
	[thanh_pho] [nvarchar](100) NULL,
	[quan_huyen] [nvarchar](100) NULL,
	[phuong_xa] [nvarchar](100) NULL,
	[ngay_tao] [datetime] NULL,
	[ngay_cap_nhap] [datetime] NULL,
	[trang_thai] [int] NULL,
	[tai_khoan_id] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[email]    Script Date: 03/02/2025 12:42:29 am ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[email](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[ma] [int] NULL,
	[tieu_de] [varchar](255) NULL,
	[mo_ta] [varchar](255) NULL,
	[ngay_tao] [datetime] NULL,
	[ngay_cap_nhap] [datetime] NULL,
	[trang_thai] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[gio_hang]    Script Date: 03/02/2025 12:42:29 am ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[gio_hang](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[don_gia] [money] NULL,
	[so_luong] [int] NULL,
	[tai_khoan_id] [bigint] NULL,
	[chi_tiet_san_pham_id] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[hinh_anh]    Script Date: 03/02/2025 12:42:29 am ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[hinh_anh](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[duong_dan] [nvarchar](1000) NULL,
	[ngay_tao] [datetime] NULL,
	[ngay_cap_nhap] [datetime] NULL,
	[trang_thai] [int] NULL,
	[san_pham_id] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[hoa_don]    Script Date: 03/02/2025 12:42:29 am ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[hoa_don](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[ma_hoa_don] [nvarchar](100) NULL,
	[ngay_tao_hoa_don] [datetime] NULL,
	[ngay_du_kien_giao_hang] [datetime] NULL,
	[ngay_giao_hang] [datetime] NULL,
	[ngay_thanh_toan] [datetime] NULL,
	[tong_tien] [money] NULL,
	[tong_tien_cuoi_cung] [money] NULL,
	[trang_thai_don_hang] [int] NULL,
	[trang_thai_thanh_toan] [int] NULL,
	[ngay_cap_nhap] [datetime] NULL,
	[phi_van_chuyen] [money] NULL,
	[ma_van_chuyen] [nvarchar](100) NULL,
	[trang_thai_tra_hang] [int] NULL,
	[ghi_chu] [nvarchar](100) NULL,
	[trang_thai] [int] NULL,
	[tai_khoan_id] [bigint] NULL,
	[ma_giam_gia_id] [bigint] NULL,
	[dia_chi_id] [bigint] NULL,
	[phuong_thuc_thanh_toan_id] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[hoa_don_chi_tiet]    Script Date: 03/02/2025 12:42:29 am ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[hoa_don_chi_tiet](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[so_luong] [int] NULL,
	[gia] [money] NULL,
	[chi_tiet_san_pham_id] [bigint] NULL,
	[hoa_don_id] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[kich_co]    Script Date: 03/02/2025 12:42:29 am ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[kich_co](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[ten] [nvarchar](100) NULL,
	[mo_ta] [nvarchar](255) NULL,
	[ngay_tao] [datetime] NULL,
	[ngay_cap_nhap] [datetime] NULL,
	[trang_thai] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ma_giam_gia]    Script Date: 03/02/2025 12:42:29 am ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ma_giam_gia](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[ten] [nvarchar](100) NULL,
	[loai_giam_gia] [nvarchar](100) NULL,
	[gia_tri_giam_gia] [money] NULL,
	[so_luong] [int] NULL,
	[ngay_tao] [datetime] NULL,
	[ngay_cap_nhap] [datetime] NULL,
	[trang_thai] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[mau_sac]    Script Date: 03/02/2025 12:42:29 am ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[mau_sac](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[ten] [nvarchar](100) NULL,
	[mo_ta] [nvarchar](255) NULL,
	[ngay_tao] [datetime] NULL,
	[ngay_cap_nhap] [datetime] NULL,
	[trang_thai] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[phuong_thuc_thanh_toan]    Script Date: 03/02/2025 12:42:29 am ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[phuong_thuc_thanh_toan](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[ten] [nvarchar](100) NULL,
	[ngay_thanh_toan] [datetime] NULL,
	[so_tien] [money] NULL,
	[trang_thai] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[san_pham]    Script Date: 03/02/2025 12:42:29 am ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[san_pham](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[ma] [varchar](30) NULL,
	[ten] [nvarchar](100) NULL,
	[mo_ta] [nvarchar](255) NULL,
	[ngay_tao] [datetime] NULL,
	[ngay_cap_nhap] [datetime] NULL,
	[trang_thai] [int] NULL,
	[danh_muc_id] [bigint] NULL,
	[chat_lieu_id] [bigint] NULL,
	[thuong_hieu_id] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[tai_khoan]    Script Date: 03/02/2025 12:42:29 am ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tai_khoan](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[ma] [varchar](30) NULL,
	[ten] [nvarchar](100) NULL,
	[Password] [varchar](1000) NULL,
	[anh_dai_dien] [varchar](255) NULL,
	[Email] [varchar](50) NULL,
	[so_dien_thoai] [varchar](15) NULL,
	[ngay_tao] [datetime] NULL,
	[ngay_cap_nhap] [datetime] NULL,
	[trang_thai] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[thuong_hieu]    Script Date: 03/02/2025 12:42:29 am ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[thuong_hieu](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[ten] [nvarchar](100) NULL,
	[mo_ta] [nvarchar](255) NULL,
	[ngay_tao] [datetime] NULL,
	[ngay_cap_nhap] [datetime] NULL,
	[trang_thai] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[vai_tro]    Script Date: 03/02/2025 12:42:29 am ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[vai_tro](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[ten] [nvarchar](100) NULL,
	[mo_ta] [nvarchar](255) NULL,
	[ngay_tao] [datetime] NULL,
	[ngay_cap_nhap] [datetime] NULL,
	[trang_thai] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[vai_tro_tai_khoan]    Script Date: 03/02/2025 12:42:29 am ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[vai_tro_tai_khoan](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[tai_khoan_id] [bigint] NOT NULL,
	[vai_tro_id] [bigint] NOT NULL
) ON [PRIMARY]
GO
SET IDENTITY_INSERT [dbo].[tai_khoan] ON 

INSERT [dbo].[tai_khoan] ([Id], [ma], [ten], [Password], [anh_dai_dien], [Email], [so_dien_thoai], [ngay_tao], [ngay_cap_nhap], [trang_thai]) VALUES (1, NULL, N'admin', N'$2a$10$JcaDrQapTOFb3w1hqDjs0.NhUuX3gsptPHtgcULeMIO81wgZgMvEu', NULL, N'buidaithang16122003@gmail.com', N'0343961281', CAST(N'2025-02-03T00:26:36.763' AS DateTime), NULL, 1)
INSERT [dbo].[tai_khoan] ([Id], [ma], [ten], [Password], [anh_dai_dien], [Email], [so_dien_thoai], [ngay_tao], [ngay_cap_nhap], [trang_thai]) VALUES (2, NULL, N'khachhang', N'$2a$10$RCae5LL2rOZH9f2CZ.CJnewwnAgb2ZBNK86z1KMIrxPMQT6p94JTu', NULL, N'buidaithang161220031@gmail.com', N'0343961282', CAST(N'2025-02-03T00:27:16.710' AS DateTime), NULL, 1)
SET IDENTITY_INSERT [dbo].[tai_khoan] OFF
GO
SET IDENTITY_INSERT [dbo].[vai_tro] ON 

INSERT [dbo].[vai_tro] ([Id], [ten], [mo_ta], [ngay_tao], [ngay_cap_nhap], [trang_thai]) VALUES (1, N'ROLE_ADMIN', N'admin', CAST(N'2025-02-03T00:24:29.000' AS DateTime), NULL, 1)
INSERT [dbo].[vai_tro] ([Id], [ten], [mo_ta], [ngay_tao], [ngay_cap_nhap], [trang_thai]) VALUES (2, N'ROLE_EMPLOYEE', N'nhân viên', CAST(N'2025-02-03T00:24:46.000' AS DateTime), NULL, 1)
INSERT [dbo].[vai_tro] ([Id], [ten], [mo_ta], [ngay_tao], [ngay_cap_nhap], [trang_thai]) VALUES (3, N'ROLE_USER', N'khách hàng', CAST(N'2025-02-03T00:25:00.000' AS DateTime), NULL, 1)
SET IDENTITY_INSERT [dbo].[vai_tro] OFF
GO
SET IDENTITY_INSERT [dbo].[vai_tro_tai_khoan] ON 

INSERT [dbo].[vai_tro_tai_khoan] ([Id], [tai_khoan_id], [vai_tro_id]) VALUES (1, 1, 1)
INSERT [dbo].[vai_tro_tai_khoan] ([Id], [tai_khoan_id], [vai_tro_id]) VALUES (2, 2, 3)
SET IDENTITY_INSERT [dbo].[vai_tro_tai_khoan] OFF
GO
ALTER TABLE [dbo].[chi_tiet_san_pham]  WITH CHECK ADD FOREIGN KEY([kich_co_id])
REFERENCES [dbo].[kich_co] ([Id])
GO
ALTER TABLE [dbo].[chi_tiet_san_pham]  WITH CHECK ADD FOREIGN KEY([mau_sac_id])
REFERENCES [dbo].[mau_sac] ([Id])
GO
ALTER TABLE [dbo].[chi_tiet_san_pham]  WITH CHECK ADD FOREIGN KEY([san_pham_id])
REFERENCES [dbo].[san_pham] ([Id])
GO
ALTER TABLE [dbo].[dia_chi]  WITH CHECK ADD FOREIGN KEY([tai_khoan_id])
REFERENCES [dbo].[tai_khoan] ([Id])
GO
ALTER TABLE [dbo].[gio_hang]  WITH CHECK ADD FOREIGN KEY([chi_tiet_san_pham_id])
REFERENCES [dbo].[chi_tiet_san_pham] ([Id])
GO
ALTER TABLE [dbo].[gio_hang]  WITH CHECK ADD FOREIGN KEY([tai_khoan_id])
REFERENCES [dbo].[tai_khoan] ([Id])
GO
ALTER TABLE [dbo].[hinh_anh]  WITH CHECK ADD FOREIGN KEY([san_pham_id])
REFERENCES [dbo].[san_pham] ([Id])
GO
ALTER TABLE [dbo].[hoa_don]  WITH CHECK ADD FOREIGN KEY([dia_chi_id])
REFERENCES [dbo].[dia_chi] ([Id])
GO
ALTER TABLE [dbo].[hoa_don]  WITH CHECK ADD FOREIGN KEY([ma_giam_gia_id])
REFERENCES [dbo].[ma_giam_gia] ([Id])
GO
ALTER TABLE [dbo].[hoa_don]  WITH CHECK ADD FOREIGN KEY([phuong_thuc_thanh_toan_id])
REFERENCES [dbo].[phuong_thuc_thanh_toan] ([Id])
GO
ALTER TABLE [dbo].[hoa_don]  WITH CHECK ADD FOREIGN KEY([tai_khoan_id])
REFERENCES [dbo].[tai_khoan] ([Id])
GO
ALTER TABLE [dbo].[hoa_don_chi_tiet]  WITH CHECK ADD FOREIGN KEY([chi_tiet_san_pham_id])
REFERENCES [dbo].[chi_tiet_san_pham] ([Id])
GO
ALTER TABLE [dbo].[hoa_don_chi_tiet]  WITH CHECK ADD FOREIGN KEY([hoa_don_id])
REFERENCES [dbo].[hoa_don] ([Id])
GO
ALTER TABLE [dbo].[san_pham]  WITH CHECK ADD FOREIGN KEY([chat_lieu_id])
REFERENCES [dbo].[chat_lieu] ([Id])
GO
ALTER TABLE [dbo].[san_pham]  WITH CHECK ADD FOREIGN KEY([danh_muc_id])
REFERENCES [dbo].[danh_muc] ([Id])
GO
ALTER TABLE [dbo].[san_pham]  WITH CHECK ADD FOREIGN KEY([thuong_hieu_id])
REFERENCES [dbo].[thuong_hieu] ([Id])
GO
ALTER TABLE [dbo].[vai_tro_tai_khoan]  WITH CHECK ADD  CONSTRAINT [FK15phj5cffij91xfqnpn3jb78j] FOREIGN KEY([vai_tro_id])
REFERENCES [dbo].[vai_tro] ([Id])
GO
ALTER TABLE [dbo].[vai_tro_tai_khoan] CHECK CONSTRAINT [FK15phj5cffij91xfqnpn3jb78j]
GO
ALTER TABLE [dbo].[vai_tro_tai_khoan]  WITH CHECK ADD  CONSTRAINT [FKeu51ua9a9kw9wp9dq91jgcog6] FOREIGN KEY([tai_khoan_id])
REFERENCES [dbo].[tai_khoan] ([Id])
GO
ALTER TABLE [dbo].[vai_tro_tai_khoan] CHECK CONSTRAINT [FKeu51ua9a9kw9wp9dq91jgcog6]
GO
USE [master]
GO
ALTER DATABASE [datntryonall] SET  READ_WRITE 
GO
